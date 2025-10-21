package request;

public class HabilitarProductoRequest {

    private int idAlmacen;
    private String sku;

    public HabilitarProductoRequest() {
    }

    public HabilitarProductoRequest(int idAlmacen, String sku) {
        this.idAlmacen = idAlmacen;
        this.sku = sku;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
