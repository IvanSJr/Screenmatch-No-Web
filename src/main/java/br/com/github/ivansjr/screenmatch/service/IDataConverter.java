package br.com.github.ivansjr.screenmatch.service;

public interface IDataConverter {

    <T> T dataConverter(String json, Class<T> tclass);

}
