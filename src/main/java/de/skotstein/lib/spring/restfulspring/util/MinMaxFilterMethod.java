package de.skotstein.lib.spring.restfulspring.util;

import java.util.Objects;

public class MinMaxFilterMethod extends FilterMethod{

    private boolean greaterThan = false;
    private boolean equalTo = false;
    private boolean matchNullValues = true;

    /**
     * Creates an instance of {@link MinMaxFilterMethod}.
     * If the 'greaterThan' argument is set to true, an entity matches the query if the entity value is greater than the query parameter value, i.e., '>'
     * If the 'greaterThan' argument is set to false, an entity matches the query if the entity value is less than the query parameter value, i.e., '<'
     * If the 'equalTo' argument is set to true, an entity also matches the query if the entity value is equal to the query parameter value, i.e., '>=' or '<='
     * Note that an entity matches the query if the entity value is null (see also {@link MinMaxFilterMethod#MinMaxFilterMethod(boolean, boolean, boolean)})
     * @param greaterThan if set to true, an entity matches the query if the entity value is greater than the query parameter value, i.e., '>'
     * @param equalTo if set to true, an entity also matches the query if the entity value is equal to the query parameter value, i.e., '>=' or '<='
     */
    public MinMaxFilterMethod(boolean greaterThan, boolean equalTo){
        this.greaterThan = greaterThan;
        this.equalTo = equalTo;
    }

    /**
     * Creates an instance of {@link MinMaxFilterMethod}.
     * If the 'greaterThan' argument is set to true, an entity matches the query if the entity value is greater than the query parameter value, i.e., '>'
     * If the 'greaterThan' argument is set to false, an entity matches the query if the entity value is less than the query parameter value, i.e., '<'
     * If the 'equalTo' argument is set to true, an entity also matches the query if the entity value is equal to the query parameter value, i.e., '>=' or '<='
     * If the 'matchNullValues' is set to true, an entity also matches the query if the entity value is null
     * If the 'matchNullValues' is set to false, an entity does not match the query if the entity value is null
     * @param greaterThan if set to true, an entity matches the query if the entity value is greater than the query parameter value, i.e., '>'
     * @param equalTo if set to true, an entity also matches the query if the entity value is equal to the query parameter value, i.e., '>=' or '<='
     * @param matchNullValues if set to false, an entity does not match the query if the entity value is null
     */
    public MinMaxFilterMethod(boolean greaterThan, boolean equalTo, boolean matchNullValues){
        this(greaterThan,equalTo);
        this.matchNullValues = matchNullValues;
    }

    @Override
    protected boolean match(String queryParameterKey, Object queryParameterValue, Object entityValue) {
        if(Objects.isNull(entityValue)){
            return this.matchNullValues;
        }
        if(isNumeric(entityValue) && isNumeric(queryParameterValue)){
            if(isInteger(entityValue) && isInteger(queryParameterValue)){
                long a = this.integerToLong(entityValue);
                long b = this.integerToLong(queryParameterValue);
                return this.match(a, b);
            }else{
                double a = this.numericToDouble(entityValue);
                double b = this.numericToDouble(queryParameterValue);
                return this.match(a, b);
            }
        }else{
            return true;
        }
    }

    private boolean isNumeric(Object value){
        return isInteger(value) || value instanceof Float || value instanceof Double;
    }

    private boolean isInteger(Object value){
        return value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long;
    }

    private long integerToLong(Object value){
        long a = 0;
        if(value instanceof Byte){
            a = ((Byte)value).byteValue();
        }else if(value instanceof Short){
            a = ((Short) value).shortValue();
        }else if((value instanceof Integer)){
            a = ((Integer) value).intValue();
        }else{
            a = ((Long) value).longValue();
        }
        return a;
    }

    private double numericToDouble(Object value){
        double a = 0;
        if(value instanceof Byte){
            a = ((Byte)value).byteValue();
        }else if(value instanceof Short){
            a = ((Short) value).shortValue();
        }else if((value instanceof Integer)){
            a = ((Integer) value).intValue();
        }else if((value instanceof Long)){
            a = ((Long) value).longValue();
        }else if((value instanceof Float)){
            a = ((Float) value).floatValue();
        }else{
            a = ((Double) value).doubleValue();
        }
        return a;
    }

    private boolean match(long a, long b){
        if(this.greaterThan){
            if(this.equalTo){
                return a >= b;
            }else{
                return a > b;
            }
        }else{
            if(this.equalTo){
                return a <= b;
            }else{
                return a < b;
            }
        }
    }

    private boolean match(double a, double b){
        if(this.greaterThan){
            if(this.equalTo){
                return a >= b;
            }else{
                return a > b;
            }
        }else{
            if(this.equalTo){
                return a <= b;
            }else{
                return a < b;
            }
        }
    }
}
