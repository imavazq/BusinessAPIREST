package com.imavazq.public_business_api_rest.mapper;

//Mapea 2 clases (A, B son genéricas)
public interface IMapper<A, B> {
    B mapTo(A a); //Mapea de clase A a clase B

    A mapFrom(B b); //Mapea de clase B a clase A
}
