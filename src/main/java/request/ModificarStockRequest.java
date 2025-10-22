package request;

public class ModificarStockRequest {
    private String sku;
    private int idAlmacen;
    private int cantidad;

    public ModificarStockRequest() {
    }

    public ModificarStockRequest(String sku, int idAlmacen, int cantidad) {
        this.sku = sku;
        this.idAlmacen = idAlmacen;
        this.cantidad = cantidad;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}