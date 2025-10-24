package request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    private String sku;
    private String nombreCategoria; 
    private String nombre;
    private String unidadMedida;
    private String descripcion;

}
