package ru.maksidom.testtask;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sends")
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "dt_send")
    private LocalDateTime date;

    @Column(name="what", nullable = false)
    private String contain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageEntity that = (PackageEntity) o;

        if (!id.equals(that.id)) return false;
        if (status != that.status) return false;
        if (!date.equals(that.date)) return false;
        return contain.equals(that.contain);
    }

}
