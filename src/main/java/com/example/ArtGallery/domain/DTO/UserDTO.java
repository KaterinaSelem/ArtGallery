package com.example.ArtGallery.domain.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private RoleDTO userRole;
    private String born_city;
    private String live_city;
    private List<String> exhibition;
    private String description;
    private String image;

//    public UserDTO(Long id, String name, String email, String password, RoleDTO roleDTO, String liveCity, String description, List<String> exhibitions, String image, String bornCity) {
//    }

//    public UserDTO() {
//
//    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBorn_city() {
        return born_city;
    }

    public String getLive_city() {
        return live_city;
    }

    public List<String> getExhibition() {
        return exhibition;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(RoleDTO userRole) {
        this.userRole = userRole;
    }

    public void setBorn_city(String born_city) {
        this.born_city = born_city;
    }

    public void setLive_city(String live_city) {
        this.live_city = live_city;
    }

    public void setExhibition(List<String> exhibition) {
        this.exhibition = exhibition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public RoleDTO getUserRole() {
        return userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(name, userDTO.name) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password) && Objects.equals(userRole, userDTO.userRole) && Objects.equals(born_city, userDTO.born_city) && Objects.equals(live_city, userDTO.live_city) && Objects.equals(exhibition, userDTO.exhibition) && Objects.equals(description, userDTO.description) && Objects.equals(image, userDTO.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, userRole, born_city, live_city, exhibition, description, image);
    }

//    public UserDTO() {
//    }

//    public UserDTO(Long id, String name, String email, String password, RoleDTO userRole, String born_city, String live_city, List<String> exhibition, String description, String image) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.userRole = userRole;
//        this.born_city = born_city;
//        this.live_city = live_city;
//        this.exhibition = exhibition;
//        this.description = description;
//        this.image = image;
//    }
}
