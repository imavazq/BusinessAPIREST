package com.imavazq.public_business_api_rest.service.impl;

import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.repository.EntryRepository;
import com.imavazq.public_business_api_rest.repository.ProductRepository;
import com.imavazq.public_business_api_rest.service.IEntryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EntryServiceImpl implements IEntryService {
    private EntryRepository entryRepository;
    private ProductRepository productRepository;

    public EntryServiceImpl(EntryRepository entryRepository, ProductRepository productRepository) {
        this.entryRepository = entryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public EntryEntity save(EntryEntity entryEntity) {
        //TODO: Agregar lógica de aumento de stock de producto referenciado
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
        //TODO: Agregar validaciones de que no sean null los no null
        entryEntity.setId(id); //aseguro que no cambie id
        return entryRepository.findById(id).map(existingEntry -> {//Si encuentra existingEntry (entry resultado)
            //Si nuevo amount de entry a actualizar no es null -> se lo asigno al entry recuperado
            //Optional.ofNullable(entryEntity.getAmount()).ifPresent(amount -> entryEntity.setAmount(amount));
            Optional.ofNullable(entryEntity.getAmount()).ifPresent(entryEntity::setAmount);
            Optional.ofNullable(entryEntity.getUnitCost()).ifPresent(entryEntity::setUnitCost);
            Optional.ofNullable(entryEntity.getTotalCost()).ifPresent(entryEntity::setTotalCost);
            Optional.ofNullable(entryEntity.getDate()).ifPresent(entryEntity::setDate);
            Optional.ofNullable(entryEntity.getProduct()).ifPresent(entryEntity::setProduct);

            //valido que product exista en la BD antes de asignarlo (decisión de diseño que no sea CASCADE)
            Optional.ofNullable(entryEntity.getProduct()).ifPresent(newProduct -> {
                ProductEntity existingProduct =
                        productRepository
                                .findById(newProduct.getId()) //busco en BD
                                .orElseThrow(() -> new RuntimeException("El producto indicado no existe.")); //si no está en la BD lanzo exception
                //TODO: Personalizar exception
                existingEntry.setProduct(existingProduct);
            });
        });
    }

    @Override
    public void delete(Long id) {
        entryRepository.deleteById(id);
    }
}
