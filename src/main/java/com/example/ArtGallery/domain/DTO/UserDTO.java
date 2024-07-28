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
    private String bornCity;  // Исправлено с bornСity
    private String liveCity;  // Исправлено с liveСity
    private List<String> exhibitions;  // Исправлено с exhibition
    private String description;
    private String image;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleDTO userRole) {
        this.userRole = userRole;
    }

    public String getBornCity() {
        return bornCity;
    }

    public void setBornCity(String bornCity) {
        this.bornCity = bornCity;
    }

    public String getLiveCity() {
        return liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public List<String> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<String> exhibitions) {
        this.exhibitions = exhibitions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(name, userDTO.name) && Objects.equals(email, userDTO.email) && Objects.equals(password, userDTO.password) && Objects.equals(userRole, userDTO.userRole) && Objects.equals(bornCity, userDTO.bornCity) && Objects.equals(liveCity, userDTO.liveCity) && Objects.equals(exhibitions, userDTO.exhibitions) && Objects.equals(description, userDTO.description) && Objects.equals(image, userDTO.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, userRole, bornCity, liveCity, exhibitions, description, image);
    }
}