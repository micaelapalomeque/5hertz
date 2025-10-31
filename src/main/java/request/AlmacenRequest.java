package request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlmacenRequest {
    private int idCentro;
    private String nombre;
    private int capacidad;
    private String estado;
}