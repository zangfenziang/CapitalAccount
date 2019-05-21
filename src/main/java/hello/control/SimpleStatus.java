package hello.control;

class SimpleStatus{
    private int status;
    private String message;
    SimpleStatus(int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus(){
        return status;
    }
    public String getMessage(){
        return message;
    }
}