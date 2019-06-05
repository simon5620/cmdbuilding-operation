package io.github.whyareyousoseriously.cmdbuildingspringbootstarter.pojo;

public class Meta {

    private Integer total;
    private Positions positions;
    private References references;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Positions getPositions() {
        return positions;
    }

    public void setPositions(Positions positions) {
        this.positions = positions;
    }

    public References getReferences() {
        return references;
    }

    public void setReferences(References references) {
        this.references = references;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Meta.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("total");
        sb.append('=');
        sb.append(((this.total == null)?"<null>":this.total));
        sb.append(',');
        sb.append("positions");
        sb.append('=');
        sb.append(((this.positions == null)?"<null>":this.positions));
        sb.append(',');
        sb.append("references");
        sb.append('=');
        sb.append(((this.references == null)?"<null>":this.references));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.total == null)? 0 :this.total.hashCode()));
        result = ((result* 31)+((this.positions == null)? 0 :this.positions.hashCode()));
        result = ((result* 31)+((this.references == null)? 0 :this.references.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return ((((this.total == rhs.total)||((this.total!= null)&&this.total.equals(rhs.total)))&&((this.positions == rhs.positions)||((this.positions!= null)&&this.positions.equals(rhs.positions))))&&((this.references == rhs.references)||((this.references!= null)&&this.references.equals(rhs.references))));
    }

}
