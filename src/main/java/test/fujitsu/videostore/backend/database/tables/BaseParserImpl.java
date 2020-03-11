package test.fujitsu.videostore.backend.database.tables;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public abstract class BaseParserImpl<T> implements BaseParser<T> {

    protected String filePath;
    protected ObjectMapper objectMapper;

    public BaseParserImpl(String filePath) {
        this.filePath = filePath;
        instantiateParser();
    }

    public void instantiateParser(){
        if (filePath.endsWith(".json")){
            objectMapper = new ObjectMapper();
        }
        else if (filePath.endsWith(".yaml")){
            objectMapper = new ObjectMapper(new YAMLFactory());
        }
        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
}
