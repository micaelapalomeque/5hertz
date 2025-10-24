package request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarMovimientoRequest {
    private int idAlmacen;
    private String sku;
    private int cantidad;
    private String tipoMovimiento;
}
