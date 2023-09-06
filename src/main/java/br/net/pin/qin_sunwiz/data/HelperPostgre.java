package br.net.pin.qin_sunwiz.data;

public class HelperPostgre extends Helper {
    @Override
    public String formNature(Field field) {
        var builder = new StringBuilder(field.name);
        switch (field.nature) {
            case BOOL:
                builder.append(" BOOLEAN");
                break;
            case BIT:
                builder.append(" BIT");
                break;
            case TINY:
                builder.append(" TINYINT");
                break;
            case SMALL:
                builder.append(" SMALLINT");
                break;
            case INT:
            case SERIAL:
                builder.append(" INTEGER");
                break;
            case LONG:
            case BIG_SERIAL:
                builder.append(" BIGINT");
                break;
            case FLOAT:
                builder.append(" FLOAT");
                break;
            case REAL:
                builder.append(" REAL");
                break;
            case DOUBLE:
                builder.append(" DOUBLE");
                break;
            case NUMERIC:
            case BIG_NUMERIC:
                builder.append(" NUMERIC");
                if (field.size != null) {
                    builder.append("(");
                    builder.append(field.size);
                    if (field.precision != null) {
                        builder.append(",");
                        builder.append(field.precision);
                    }
                    builder.append(")");
                }
                break;
            case CHAR:
                builder.append(" CHAR(1)");
                break;
            case CHARS:
                builder.append(" VARCHAR");
                if (field.size != null) {
                    builder.append("(");
                    builder.append(field.size);
                    builder.append(")");
                }
                break;
            case DATE:
                builder.append(" DATE");
                break;
            case TIME:
                builder.append(" TIME");
                break;
            case DATE_TIME:
            case TIMESTAMP:
                builder.append(" TIMESTAMP");
                break;
            case TEXT:
                builder.append(" TEXT");
                if (field.size != null) {
                    builder.append("(");
                    builder.append(field.size);
                    builder.append(")");
                }
                break;
            case BYTES:
                builder.append(" BYTEA");
                if (field.size != null) {
                    builder.append("(");
                    builder.append(field.size);
                    builder.append(")");
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (field.notNull) {
            builder.append(" NOT NULL");
        }
        return builder.toString();
    }

    @Override
    public String formCondition(Filter.Likes condition, String with) {
        switch (condition) {
            case EQUALS:
                return " = " + with + " ";
            case BIGGER:
                return " > " + with + " ";
            case LESSER:
                return " < " + with + " ";
            case BIGGER_EQUALS:
                return " >= " + with + " ";
            case LESSER_EQUALS:
                return " <= " + with + " ";
            case STARTS_WITH:
                return " LIKE " + with + " || '%' ";
            case ENDS_WITH:
                return " LIKE '%' || " + with + " ";
            case CONTAINS:
                return " LIKE '%' || " + with + " || '%' ";
            default:
                throw new UnsupportedOperationException();
        }
    }
}
