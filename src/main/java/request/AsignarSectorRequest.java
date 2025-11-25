package request;

public class AsignarSectorRequest {
    private String sku;
    private int idAlmacen;
    private Integer idSector;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(int idAlmacen) { this.idAlmacen = idAlmacen; }
    public Integer getIdSector() { return idSector; }
    public void setIdSector(Integer idSector) { this.idSector = idSector; }
}
