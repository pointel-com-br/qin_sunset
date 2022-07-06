package br.net.pin.qin_sunwiz.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.net.pin.qin_sunwiz.data.Filter.Seems;
import br.net.pin.qin_sunwiz.mage.WizData;

public abstract class Helper {
  public List<Registry> getHeads(Connection link) throws Exception {
    var meta = link.getMetaData();
    var set = meta.getTables(null, null, "%", new String[] { "TABLE" });
    var result = new ArrayList<Registry>();
    while (set.next()) {
      result.add(new Registry(set.getString(1), set.getString(2), set.getString(3)));
    }
    return result;
  }

  public void create(Connection connection, Table table) throws Exception {
    this.create(connection, table, false);
  }

  public void create(Connection connection, Table table, boolean ifNotExists)
      throws Exception {
    var builder = new StringBuilder();
    builder.append("CREATE TABLE ");
    if (ifNotExists) {
      builder.append("IF NOT EXISTS ");
    }
    builder.append(table.getCatalogSchemaName());
    builder.append(" (");
    for (var i = 0; i < table.fields.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(this.formNature(table.fields.get(i)));
    }
    builder.append(")");
    connection.createStatement().execute(builder.toString());
  }

  public ResultSet select(Connection link, Select select) throws Exception {
    var builder = new StringBuilder("SELECT ");
    var fromSource = select.registry.getCatalogSchemaName();
    var fromAlias = select.registry.alias != null ? select.registry.alias : fromSource;
    if (select.fields == null || select.fields.isEmpty()) {
      builder.append("*");
    } else {
      for (var i = 0; i < select.fields.size(); i++) {
        if (i > 0) {
          builder.append(", ");
        }
        if (!select.fields.get(i).name.contains(".")) {
          builder.append(fromAlias);
          builder.append(".");
        }
        builder.append(select.fields.get(i).name);
      }
    }
    builder.append(" FROM ");
    builder.append(fromSource);
    if (select.registry.alias != null) {
      builder.append(" AS ");
      builder.append(fromAlias);
    }
    if (select.hasJoins()) {
      for (var join : select.joins) {
        if (join.ties != null) {
          builder.append(join.ties.toString());
        }
        builder.append(" JOIN ");
        var withSource = join.registry.getCatalogSchemaName();
        var withAlias = withSource;
        builder.append(withSource);
        if (join.alias != null) {
          builder.append(" AS ");
          withAlias = join.alias;
          builder.append(withAlias);
        } else if (join.registry.alias != null) {
          builder.append(" AS ");
          withAlias = join.registry.alias;
          builder.append(withAlias);
        }
        if (join.hasFilters()) {
          builder.append(" ON ");
          builder.append(this.formClauses(join.filters, fromAlias, withAlias));
        }
      }
    }
    if (select.hasFilters()) {
      builder.append(" WHERE ");
      builder.append(this.formClauses(select.filters, fromAlias, null));
    }
    if (select.limit != null) {
      builder.append(" LIMIT ");
      builder.append(select.limit);
    }
    if (select.offset != null) {
      builder.append(" OFFSET ");
      builder.append(select.offset);
    }
    var build = builder.toString();
    System.out.println("SELECT: " + build);
    var prepared = link.prepareStatement(build);
    var param_index = 1;
    if (select.hasJoins()) {
      for (var join : select.joins) {
        if (join.hasFilters()) {
          for (var clause : join.filters) {
            if (clause.valued != null && clause.valued.data != null) {
              this.setParameter(prepared, param_index, clause.valued);
              param_index++;
            }
          }
        }
      }
    }
    if (select.hasFilters()) {
      for (var clause : select.filters) {
        if (clause.valued != null && clause.valued.data != null) {
          this.setParameter(prepared, param_index, clause.valued);
          param_index++;
        }
      }
    }

    return prepared.executeQuery();
  }

  public int insert(Connection link, Insert insert) throws Exception {
    var builder = new StringBuilder("INSERT INTO ");
    builder.append(insert.registry.getCatalogSchemaName());
    builder.append(" (");
    for (var i = 0; i < insert.valueds.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(insert.valueds.get(i).name);
    }
    builder.append(") VALUES (");
    for (var i = 0; i < insert.valueds.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      final var value = insert.valueds.get(i);
      if (value.data != null) {
        builder.append("?");
      } else {
        builder.append("NULL");
      }
    }
    builder.append(")");
    var build = builder.toString();
    System.out.println("INSERT: " + build);
    var prepared = link.prepareStatement(build);
    var param_index = 1;
    for (var valued : insert.valueds) {
      if (valued != null) {
        this.setParameter(prepared, param_index, valued);
        param_index++;
      }
    }
    return prepared.executeUpdate();
  }

  public int update(Connection link, Update update) throws Exception {
    var builder = new StringBuilder("UPDATE ");
    builder.append(update.registry.getCatalogSchemaName());
    builder.append(" SET ");
    for (var i = 0; i < update.valueds.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(update.valueds.get(i).name);
      builder.append(" = ");
      if (update.valueds.get(i).data == null) {
        builder.append("NULL");
      } else {
        builder.append("?");
      }
    }
    builder.append(" WHERE ");
    builder.append(this.formClauses(update.filters, null, null));
    if (update.limit != null) {
      builder.append(" LIMIT ");
      builder.append(update.limit);
    }
    var build = builder.toString();
    System.out.println("UPDATE: " + build);
    var prepared = link.prepareStatement(build);
    var param_index = 1;
    for (var valued : update.valueds) {
      if (valued != null) {
        this.setParameter(prepared, param_index, valued);
        param_index++;
      }
    }
    if (update.filters != null && !update.filters.isEmpty()) {
      for (var clause : update.filters) {
        if (clause.valued != null) {
          this.setParameter(prepared, param_index, clause.valued);
          param_index++;
        }
      }
    }
    return prepared.executeUpdate();
  }

  public int delete(Connection link, Delete delete) throws Exception {
    var builder = new StringBuilder("DELETE FROM ");
    builder.append(delete.registry.getCatalogSchemaName());
    builder.append(" WHERE ");
    builder.append(this.formClauses(delete.filters, null, null));
    if (delete.limit != null) {
      builder.append(" LIMIT ");
      builder.append(delete.limit);
    }
    var build = builder.toString();
    System.out.println("DELETE: " + build);
    var prepared = link.prepareStatement(build);
    var param_index = 1;
    if (delete.filters != null && !delete.filters.isEmpty()) {
      for (var clause : delete.filters) {
        if (clause.valued.data != null) {
          this.setParameter(prepared, param_index, clause.valued);
          param_index++;
        }
      }
    }
    return prepared.executeUpdate();
  }

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
      case BYTES:
      case BLOB:
        builder.append(" BLOB");
        if (field.size != null) {
          builder.append("(");
          builder.append(field.size);
          builder.append(")");
        }
        break;
      case TEXT:
        builder.append(" TEXT");
        if (field.size != null) {
          builder.append("(");
          builder.append(field.size);
          builder.append(")");
        }
        break;
      default:
        throw new UnsupportedOperationException();
    }
    if (Objects.equals(field.notNull, true)) {
      builder.append(" NOT NULL");
    }
    return builder.toString();
  }

  public String formClauses(List<Filter> filters, String from, String with) {
    if ((filters == null) || filters.isEmpty()) {
      return "";
    }
    var builder = new StringBuilder();
    var nextIsOr = false;
    for (var i = 0; i < filters.size(); i++) {
      var clause = filters.get(i);
      if (clause.valued == null && clause.linked == null) {
        continue;
      }
      if (i > 0) {
        builder.append(nextIsOr ? " OR " : " AND ");
      }
      if (clause.seems == Seems.DIVERSE) {
        builder.append(" NOT ");
      }
      if (clause.valued != null) {
        if (from != null && !from.isEmpty()) {
          builder.append(from);
          builder.append(".");
        }
        builder.append(clause.valued.name);
        if (clause.valued.data == null) {
          builder.append(" IS NULL ");
        } else {
          builder.append(this.formCondition(clause.likes, "?"));
        }
      } else if (clause.linked != null) {
        if (from != null && !from.isEmpty()) {
          builder.append(from);
          builder.append(".");
        }
        builder.append(clause.linked.name);
        if (clause.linked.with == null) {
          builder.append(" IS NULL ");
        } else {
          var formWith = new StringBuilder();
          if (with != null && !with.isEmpty()) {
            formWith.append(with);
            formWith.append(".");
          }
          formWith.append(clause.linked.with);
          builder.append(this.formCondition(clause.likes, formWith.toString()));
        }
      }
      nextIsOr = clause.ties == Filter.Ties.OR;
    }
    return builder.toString();
  }

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
        return " STARTS WITH " + with + " ";
      case ENDS_WITH:
        return " ENDS WITH " + with + " ";
      case CONTAINS:
        return " CONTAINS " + with + " ";
      default:
        throw new UnsupportedOperationException();
    }
  }

  public void setParameter(PreparedStatement prepared, int index, Valued valued)
      throws Exception {
    if (valued.type == null) {
      prepared.setObject(index, valued.data);
    } else {
      switch (valued.type) {
        case BOOL:
          prepared.setBoolean(index, WizData.getBoolean(valued.data));
          break;
        case BIT:
        case BYTE:
          prepared.setByte(index, WizData.getByte(valued.data));
          break;
        case TINY:
        case SMALL:
          prepared.setShort(index, WizData.getShort(valued.data));
          break;
        case INT:
        case SERIAL:
          prepared.setInt(index, WizData.getInteger(valued.data));
          break;
        case LONG:
        case BIG_SERIAL:
          prepared.setLong(index, WizData.getLong(valued.data));
          break;
        case FLOAT:
        case REAL:
          prepared.setFloat(index, WizData.getFloat(valued.data));
          break;
        case DOUBLE:
        case NUMERIC:
          prepared.setDouble(index, WizData.getDouble(valued.data));
          break;
        case BIG_NUMERIC:
          prepared.setBigDecimal(index, WizData.getBigDecimal(valued.data));
          break;
        case CHAR:
        case CHARS:
          prepared.setString(index, WizData.getString(valued.data));
          break;
        case DATE:
          prepared.setDate(index, WizData.getDate(valued.data));
          break;
        case TIME:
          prepared.setTime(index, WizData.getTime(valued.data));
          break;
        case DATE_TIME:
        case TIMESTAMP:
          prepared.setTimestamp(index, WizData.getTimestamp(valued.data));
          break;
        case BYTES:
          prepared.setBytes(index, WizData.getBytes(valued.data));
          break;
        case BLOB:
        case TEXT:
          prepared.setBlob(index, WizData.getBlob(valued.data));
          break;
        default:
          throw new UnsupportedOperationException();
      }
    }
  }

  public boolean isPrimaryKey(Exception error) {
    return error.getMessage().contains("unique constraint");
  }

  public static Helper instance = new Helper() {
  };
}
