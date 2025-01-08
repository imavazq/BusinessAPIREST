package com.imavazq.public_business_api_rest.controller;

import com.imavazq.public_business_api_rest.domain.dto.EntryDto;
import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import com.imavazq.public_business_api_rest.service.IEntryService;
import org.springframework.data.jpa.domain.AbstractAuditable_;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class EntryController {
    private IEntryService entryService;
    private IMapper<EntryEntity, EntryDto> entryMapper;

    public EntryController(IEntryService entryService, IMapper<EntryEntity, EntryDto> entryMapper) {
        this.entryService = entryService;
        this.entryMapper = entryMapper;
    }

    @PostMapping(path = "/api/v1/entry")
    public ResponseEntity<EntryDto> createEntry(@RequestBody EntryDto entryDto){
        EntryEntity entryEntity = entryMapper.mapFrom(entryDto);
        EntryEntity savedEntryEntity = entryService.save(entryEntity);

        EntryDto savedEntryDto = entryMapper.mapTo(savedEntryEntity);
        return new ResponseEntity<>(savedEntryDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/entries")
    public List<EntryDto> listEntries(){
        List<EntryEntity> entries = entryService.findAll();

        return entries.stream()
                .map(entryEntity -> entryMapper.mapTo(entryEntity))// === entryMapper::mapTo
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/api/v1/entry/{id}")
    public ResponseEntity<EntryDto> getEntry(@PathVariable("id") Long id){
        Optional<EntryEntity> foundEntry = entryService.findOne(id);

        return foundEntry.map(entryEntity -> {
            EntryDto entryDto = entryMapper.mapTo(entryEntity);
            return new ResponseEntity<>(entryDto, HttpStatus.OK); //Devuelvo dto con status 201 OK
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND) //Si Optional está vacío devuelvo sólo status 404 NOT FOUND
        );
    }

    @PutMapping(path = "/api/v1/entry/{id}")
    public ResponseEntity<EntryDto> fullUpdateEntry(
            @PathVariable("id") Long id, @RequestBody EntryDto entryDto
    ){
        if(!entryService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        entryDto.setId(id);//aseguro que id no cambie
        EntryEntity entryEntity = entryMapper.mapFrom(entryDto);
        //Hago update
        EntryEntity savedEntryEntity = entryService.save(entryEntity);
        EntryDto savedEntryDto = entryMapper.mapTo(savedEntryEntity);

        return new ResponseEntity<>(savedEntryDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/api/v1/entry/{id}")
    public ResponseEntity<EntryDto> partialUpdateEntry(
            @PathVariable("id") Long id, @RequestBody EntryDto entryDto
    ){
        if(!entryService.isExists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        EntryEntity entryEntity = entryMapper.mapFrom(entryDto);
        //Hago partial update
        EntryEntity updatedEntryEntity = entryService.partialUpdate(id, entryEntity);
        EntryDto updatedEntryDto = entryMapper.mapTo(updatedEntryEntity);

        return new ResponseEntity<>(updatedEntryDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/v1/entry/{id}")
    public ResponseEntity deleteEntry(@PathVariable("id") Long id){
        entryService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //status 204 NO CONTENT
    }
}
