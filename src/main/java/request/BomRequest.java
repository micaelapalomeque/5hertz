package request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BomRequest {
    private String skuProductoFinal;
    private String skuMaterial;
    private int cantPorUnidad;
}