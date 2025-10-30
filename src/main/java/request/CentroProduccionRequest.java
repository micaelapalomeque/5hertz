package request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentroProduccionRequest {
    private String sucursal;
    private String descripcion;
}