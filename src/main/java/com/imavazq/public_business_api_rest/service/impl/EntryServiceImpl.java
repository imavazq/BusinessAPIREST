package com.imavazq.public_business_api_rest.service.impl;

import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.exception.EntityNotFoundException;
import com.imavazq.public_business_api_rest.repository.EntryRepository;
import com.imavazq.public_business_api_rest.service.IEntryService;
import com.imavazq.public_business_api_rest.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EntryServiceImpl implements IEntryService {
    private EntryRepository entryRepository;

    //uso Service aunque sólo valido cosas simples como su existencia para mantener segmentación/abtracción de capas
    private IProductService productService;

    public EntryServiceImpl(EntryRepository entryRepository, IProductService productService) {
        this.entryRepository = entryRepository;
        this.productService = productService;
    }

    private void calculateTotalCost(EntryEntity entryEntity) {
        entryEntity.setTotalCost(entryEntity.getAmount() * entryEntity.getUnitCost());
    }

    @Override
    public EntryEntity save(EntryEntity entryEntity) {
        if(!productService.isExists(entryEntity.getProduct().getId()))
            throw new EntityNotFoundException("El producto indicado no existe.", "product"); //si no está en la BD lanzo exception
        //TODO: Personalizar exception
        calculateTotalCost(entryEntity); //actualizo costo total del entry en base a amount y unit cost

        //Agregar lógica de aumento de stock de producto referenciado (llevaría separar en service layer creación de full update en lugar de un único método save)

        return entryRepository.save(entryEntity);
    }


    @Override
    public List<EntryEntity> findAll() {
        //Aplico stream porque findAll() devuelve un Iterator, no la List de forma directa
        return StreamSupport.stream(entryRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EntryEntity> findOne(Long id) {
        return entryRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return entryRepository.existsById(id);
    }

    @Override
    public EntryEntity partialUpdate(Long id, EntryEntity entryEntity) {
        entryEntity.setId(id); //aseguro que no cambie id
        return entryRepository.findById(id).map(existingEntry -> {//Si encuentra existingEntry (entry resultado)
            //Si nuevo amount de entry a actualizar no es null -> se lo asigno al entry recuperado
            //Optional.ofNullable(entryEntity.getAmount()).ifPresent(amount -> existingEntry.setAmount(amount));
            Optional.ofNullable(entryEntity.getAmount()).ifPresent(existingEntry::setAmount);
            Optional.ofNullable(entryEntity.getUnitCost()).ifPresent(existingEntry::setUnitCost);
            //Optional.ofNullable(entryEntity.getTotalCost()).ifPresent(existingEntry::setTotalCost);
            calculateTotalCost(existingEntry);
            Optional.ofNullable(entryEntity.getDate()).ifPresent(existingEntry::setDate);

            //valido que product exista en la BD antes de asignarlo (decisión de diseño que no sea CASCADE)
            Optional.ofNullable(entryEntity.getProduct()).ifPresent(newProduct -> {
                ProductEntity existingProduct =
                        productService
                                .findOne(newProduct.getId()) //busco en BD
                                .orElseThrow(() -> new EntityNotFoundException("El producto indicado no existe.", "product")); //si no está en la BD lanzo exception
                //TODO: Personalizar exception
                existingEntry.setProduct(existingProduct);
            });

            //Actualizo entry recuperado
            return entryRepository.save(existingEntry);
        }).orElseThrow(() -> new EntityNotFoundException("El entry no existe.", "entry")); //No se encontró el entry en la bd
    }

    @Override
    public void delete(Long id) {
        entryRepository.deleteById(id);
    }
}
