package de.skotstein.lib.spring.restfulspring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Stack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;

public class RestClientImpl implements RestClient, RestClientTransition{
    
    private ObjectMapper objectMapper = new ObjectMapper();
    private Stack<RestClientResult> resultStack = new Stack<RestClientResult>();

    private Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
    private Map<String, Object> queryParameters = new HashMap<String, Object>();

    private String basePath = "";

    private String linksPropertyKey = "_links";
    private boolean requestExceptionEnabled = true;
    
    public RestClientImpl(){

    }

    @Override
    public RestClient withLinksProperty(String key){
        this.linksPropertyKey = key;
        return this;
    }

    @Override
    public RestClient addHeader(String name, String value){
        if(!Objects.isNull(name) && !Objects.isNull(value) && !name.isBlank() && !value.isBlank()){
            //headers.add(new SimpleEntry<String,String>(name, value));
            if(requestHeaders.containsKey(name)){
                requestHeaders.get(name).add(value);
            }else{
                List<String> values = List.of(value);
                requestHeaders.put(name, values);
            }
        }
        return this;
    }

    @Override
    public RestClient accept(String mimeType) {
        if(!Objects.isNull(mimeType)){
            List<String> values = List.of(mimeType);
            requestHeaders.put("Accept", values);
        }
        return this;
    }

    @Override
    public RestClient contentType(String mimeType) {
        if(!Objects.isNull(mimeType)){
            List<String> values = List.of(mimeType);
            requestHeaders.put("Content-Type", values);
        }
        return this;
    }

    @Override
    public RestClient clearHeaders(){
        requestHeaders.clear();
        return this;
    }

    @Override
    public RestClient removeHeader(String name){
        requestHeaders.remove(name);
        return this;
    }

    @Override
    public RestClient withBase(String basePath) {
        if(Objects.isNull(basePath)){
            this.basePath = "";
        }else{
            this.basePath = basePath;
        }
        return this;
    }

    
    @Override
    public RestClient addParameter(String key, Object value) {
        if(!Objects.isNull(key) && !Objects.isNull(value) && !key.isBlank()){
            queryParameters.put(key, value);
        }
        return this;
    }

    @Override
    public RestClient removeParameter(String key) {
        queryParameters.remove(key);
        return this;
    }

    @Override
    public RestClient clearParameters() {
        queryParameters.clear();
        return this;
    }
    
    @Override
    public RestClient withRequestException(boolean requestExceptionEnabled) {
        this.requestExceptionEnabled = requestExceptionEnabled;
        return this;
    }


    @Override
    public RestClient get(String uri) throws RestClientIOException, RestClientRequestException{
        invoke(uri, "GET");
        return this;
    }

    @Override
    public RestClient post(String uri) throws RestClientIOException, RestClientRequestException{
        invoke(uri, "POST");
        return this;
    }

    @Override
    public RestClient post(String uri, byte[] requestContent) throws RestClientIOException, RestClientRequestException{
        invoke(uri, "POST", requestContent);
        return this;
    }

    @Override
    public RestClient put(String uri, byte[] requestContent) throws RestClientIOException, RestClientRequestException{
        invoke(uri, "PUT", requestContent);
        return this;
    }

    @Override
    public RestClient delete(String uri) throws RestClientIOException, RestClientRequestException{
        invoke(uri, "DELETE");
        return this;
    }

    /**
     * Performs an HTTP request that has no request body
     * @param uri
     * @throws RestClientIOException
     * @throws RestClientRequestException
     */
    private void invoke(String uri, String method) throws RestClientIOException, RestClientRequestException{
        RestClientResult result = new RestClientResult();
        uri = this.basePath+uri;
        
        result.setUri(uri);
        
        try{
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            //set method
            connection.setRequestMethod(method);

            //add request header (if specified)
            /*
            for (SimpleEntry<String,String> header : headers) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            */
            for(String name : requestHeaders.keySet()){
                for (String value : requestHeaders.get(name)) {
                    connection.setRequestProperty(name, value);
                }
            }

            if(method.compareTo("POST")==0 || method.compareTo("PUT")==0){
                connection.setDoOutput(true);
                connection.getOutputStream().close();
            }

            int status = connection.getResponseCode();

            //write status code to results
            result.setStatusCode(status);
            //write status message to results
            result.setMessage(connection.getResponseMessage());

            //read headers
            result.setResponseHeaders(connection.getHeaderFields());

            InputStream errorStream = connection.getErrorStream();
            if(Objects.isNull(errorStream)){
                //successful case
                if(status != 204){
                    result.setResponseContent(connection.getInputStream().readAllBytes());
                }
            }else{
                //error case
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                StringBuffer errorContent = new StringBuffer();
                String errorInputLine = null;
                while((errorInputLine = bufferedReader.readLine())!=null){
                    errorContent.append(errorInputLine);
                }
                bufferedReader.close();
                if(requestExceptionEnabled){
                    RestClientRequestException re = new RestClientRequestException("The request '"+method+" "+uri+"' results into a status code '"+status+"'.");
                    re.setRestClientResult(result);
                    throw re;
                }else{
                    result.setErrorContent(errorContent.toString());
                }
                
            }
            connection.disconnect();
        }catch(IOException ioe){
            throw new RestClientIOException(ioe.getMessage(),HttpStatus.BAD_GATEWAY);
        }
        resultStack.add(result);
    }

    /**
     * Performs an HTTP request that has a request body
     * @param uri
     * @param method
     * @param requestContent
     * @throws RestClientIOException
     */
    private void invoke(String uri, String method, byte[] requestContent) throws RestClientIOException, RestClientRequestException{
        RestClientResult result = new RestClientResult();
        uri = this.basePath+uri;
        result.setUri(uri);

        try{

            //build query string
            String query = null;
            for (Entry<String,Object> entry : queryParameters.entrySet()) {
                if(Objects.isNull(query)){
                    query = "?"+entry.getKey()+"="+entry.getValue();
                }else{
                    query+="&"+entry.getKey()+"="+entry.getValue();
                }
            }
            if(!Objects.isNull(query)){
                uri+=query;
            }
            
            URL url = new URL(uri);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            //set method
            connection.setRequestMethod(method);

            //add request header (if specified)
            /*
            for (SimpleEntry<String,String> header : headers) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            */
            for(String name : requestHeaders.keySet()){
                for (String value : requestHeaders.get(name)) {
                    connection.setRequestProperty(name, value);
                }
            }


            //write request payload
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(requestContent);
            os.flush();
            os.close();
            
            //write status code to results
            int status = connection.getResponseCode();
            result.setStatusCode(status);
            //write status message to results
            result.setMessage(connection.getResponseMessage());

            //read headers
            result.setResponseHeaders(connection.getHeaderFields());

            InputStream errorStream = connection.getErrorStream();
            if(Objects.isNull(errorStream)){
                //successful case
                if(status != 204){
                    result.setResponseContent(connection.getInputStream().readAllBytes());
                }
            }else{
                //error case
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
                StringBuffer errorContent = new StringBuffer();
                String errorInputLine = null;
                while((errorInputLine = bufferedReader.readLine())!=null){
                    errorContent.append(errorInputLine);
                }
                bufferedReader.close();
                if(requestExceptionEnabled){
                    RestClientRequestException re = new RestClientRequestException("The request '"+method+" "+uri+"' results into a status code '"+status+"'.");
                    re.setRestClientResult(result);
                    throw re;
                }else{
                    result.setErrorContent(errorContent.toString());
                }
            }
            connection.disconnect();
        }catch(IOException ioe){
            throw new RestClientIOException(ioe.getMessage(),HttpStatus.BAD_GATEWAY);
        }
        resultStack.add(result);
    }

    @Override
    public RestClientTransition follow(String rel) throws RestClientExpectationFailedException, RestClientSchemaException{
        return follow(rel, null);
    }

    
    @Override
    public RestClientTransition follow(String rel, String basePath) throws RestClientExpectationFailedException, RestClientSchemaException{
        RestClientResult result = new RestClientResult();
        
        RestClientResult lastResult = result();

        if(lastResult.getResponseContent() != null){
            JsonNode jsonNode = null;
            try{
                jsonNode = objectMapper.readTree(lastResult.getResponseContent());
            }catch(IOException ioe){
                throw new RestClientExpectationFailedException("Cannot query response payload since it does not contain a JSON structure.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String uri = null;
            for(JsonNode linkNode : findLinksNodes(jsonNode)){
                uri = findUri(linkNode, rel);
                if(uri != null){
                    if(!Objects.isNull(basePath)){
                        if(basePath.endsWith("/") && uri.startsWith("/")){
                            uri = basePath.substring(0, basePath.length()-1)+uri;
                        }else{
                            uri = basePath+uri;
                        }
                    }
                    result.setUri(uri);
                    this.resultStack.add(result);
                    return this;
                }
            }
            throw new RestClientExpectationFailedException("The remote server does not advertise the preferred relation in current state", HttpStatus.CONFLICT);
        }else{
            throw new RestClientExpectationFailedException("Cannot query response payload since it is empty.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public RestClientTransition followItem(String collectionKey, String itemSelectorKey, String itemSelectorValue, String rel) throws RestClientExpectationFailedException, RestClientSchemaException, RestClientItemNotFoundException{
        RestClientResult result = new RestClientResult();
        
        RestClientResult lastResult = result();

        if(lastResult.getResponseContent() != null){
            JsonNode jsonNode = null;
            try{
                jsonNode = objectMapper.readTree(lastResult.getResponseContent());
            }catch(IOException ioe){
                throw new RestClientExpectationFailedException("Cannot query response payload since it does not contain a JSON structure.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(jsonNode.has(collectionKey) && jsonNode.get(collectionKey).isArray()){
                for (JsonNode jsonItem : jsonNode.get(collectionKey)) {
                    if(jsonItem.has(itemSelectorKey) && !jsonItem.get(itemSelectorKey).isNull()){
                        if(jsonItem.get(itemSelectorKey).asText().compareTo(itemSelectorValue)==0){
                            String uri = null;
                            for(JsonNode linkNode : findLinksNodes(jsonItem)){
                                uri = findUri(linkNode, rel);
                                if(uri != null){
                                    result.setUri(uri);
                                    this.resultStack.add(result);
                                    return this;
                                }
                            }
                            throw new RestClientExpectationFailedException("The remote server does not advertise the preferred relation in current state", HttpStatus.CONFLICT);
                        }
                    }
                }
                throw new RestClientItemNotFoundException("There is no item matching the passed key-value pair.");
            }else{
                throw new RestClientSchemaException("Response payload does contain a JSON property named '"+collectionKey+"'.",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            throw new RestClientExpectationFailedException("Cannot query response payload since it is empty.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RestClientResult result(){
        if(!this.resultStack.empty()){
            return this.resultStack.peek();
        }else{
            return null;
        }
    }

    @Override
    public List<RestClientResult> results(){
        return new ArrayList<RestClientResult>(this.resultStack);
    }

    @Override
    public RestClient get() throws RestClientIOException, RestClientRequestException{
        this.invoke(this.result().getUri(), "GET");
        return this;
    }

    @Override
    public RestClient post() throws RestClientIOException, RestClientRequestException{
        this.invoke(this.result().getUri(), "POST");
        return this;
    }

    @Override
    public RestClient post(byte[] requestContent) throws RestClientIOException, RestClientRequestException{
        this.invoke(this.result().getUri(), "POST", requestContent);
        return this;
    }

    @Override
    public RestClient put(byte[] requestContent) throws RestClientIOException, RestClientRequestException{
        this.invoke(this.result().getUri(), "PUT", requestContent);
        return this;
    }

    @Override
    public RestClient delete() throws RestClientIOException, RestClientRequestException{
        this.invoke(this.result().getUri(), "DELETE");
        return this;
    }

    
    private List<JsonNode> findLinksNodes(JsonNode inNode) throws RestClientSchemaException{
        List<JsonNode> results = new ArrayList<JsonNode>();
        if(inNode.has(this.linksPropertyKey) && inNode.get(this.linksPropertyKey).isArray()){
            results.add(inNode.get(this.linksPropertyKey));
        }
        for (JsonNode childNode : inNode) {
            if(childNode.isObject() && !childNode.isArray()){
                results.addAll(findLinksNodes(childNode));
            }
        }
        return results;
    }

    private String findUri(JsonNode linksNode, String rel) throws RestClientExpectationFailedException, RestClientSchemaException{
        for (JsonNode linkNode : linksNode) {
            if(linkNode.has("rel") && linkNode.get("href").isTextual()){
                if(linkNode.get("rel").asText().compareTo(rel)==0){
                    if(linkNode.has("href") && linkNode.get("href").isTextual()){
                        return linkNode.get("href").asText();
                    }else{
                        throw new RestClientSchemaException("The payload contains the preferred relation, but its 'href' is malformed.");
                    }
                }
            }
        }
        return null;
    }


    
}
