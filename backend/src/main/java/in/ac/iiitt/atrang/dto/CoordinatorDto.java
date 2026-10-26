package in.ac.iiitt.atrang.dto;

public class CoordinatorDto {
    private String name;
    private String phone;

    public CoordinatorDto() {}

    public CoordinatorDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
