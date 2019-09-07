package entity;


public class Field {
    private String fieldName;
    private int size;
    private String comment;
    private Type fieldType;
    private Stint stint;

    public Stint getStint() {
        return stint;
    }

    public void setStint(Stint stint) {
        this.stint = stint;
    }

    public Type getFieldType() {
        return fieldType;
    }

    public void setFieldType(Type fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
/*
    public HashMap<String, String> getType() {
        return type;
    }

    public void setType(HashMap<String, String> type) {
        this.type = type;
    }

    public HashMap<String, String> getStint() {
        return stint;
    }

    public void setStint(HashMap<String, String> stint) {
        this.stint = stint;
    }*/


}


