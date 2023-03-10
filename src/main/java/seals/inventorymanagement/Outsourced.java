package seals.inventorymanagement;

/***
 * This class represents when an outsourced part being created
 */
public class Outsourced extends Part {
    private String companyName;

    //CONSTRUCTOR EXTENDS COMPANY NAME//
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    //GETTER && SETTER//

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
