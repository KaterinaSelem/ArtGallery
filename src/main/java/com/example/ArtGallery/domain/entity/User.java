package com.example.ArtGallery.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "password")
    @NonNull
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role userRole;

    @Column(name = "born")
    private String born_city;

    @Column(name = "live")
    private String live_city;

    @Column(name = "exhibitions")
    private List<String> exhibitions = new ArrayList<>();;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

//    public User(Long id, String name, String email, String password, Role userRole, String born_city, String live_city, List<String> exhibitions, String description, String image) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.userRole = userRole;
//        this.born_city = born_city;
//        this.live_city = live_city;
//        this.exhibitions = exhibitions;
//        this.description = description;
//        this.image = image;
//    }

//    public User() {
//
//    }

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

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public String getBorn_city() {
        return born_city;
    }

    public void setBorn_city(String born_city) {
        this.born_city = born_city;
    }

    public String getLive_city() {
        return live_city;
    }

    public void setLive_city(String live_city) {
        this.live_city = live_city;
    }

    public List<String> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<String> exhibitions) {
        this.exhibitions = new ArrayList<>();
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
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(userRole, user.userRole) && Objects.equals(born_city, user.born_city) && Objects.equals(live_city, user.live_city) && Objects.equals(exhibitions, user.exhibitions) && Objects.equals(description, user.description) && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, userRole, born_city, live_city, exhibitions, description, image);
    }
}
