package Enotes_API_Service.enums;

public enum TodoStatus {

    NOT_STARTED(1, "Not Started"), IN_PROGRESS(2, "In Progress"), COMPLETED(3, "Completed");

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    TodoStatus(int i, String s) {
        this.id = i;
        this.name = s;
    }
}
