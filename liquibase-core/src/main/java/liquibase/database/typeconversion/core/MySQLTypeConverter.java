package liquibase.database.typeconversion.core;

import liquibase.database.structure.type.BlobType;
import liquibase.database.structure.type.BooleanType;
import liquibase.database.structure.type.ClobType;
import liquibase.database.structure.type.NumberType;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.structure.type.DataType;
import liquibase.database.structure.type.DateTimeType;

public class MySQLTypeConverter extends AbstractTypeConverter {

    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    public boolean supports(Database database) {
        return database instanceof MySQLDatabase;
    }

    @Override
    public BooleanType getBooleanType() {
        return new BooleanType.NumericBooleanType("TINYINT(1)");
    }

    @Override
    public ClobType getClobType() {
        return new ClobType("LONGTEXT");
    }

    @Override
    public BlobType getLongBlobType() {
    	return new BlobType("LONGBLOB");
    }

    @Override
    public NumberType getNumberType() {
        return new NumberType("NUMERIC");
    }

    @Override
    protected DataType getDataType(String columnTypeString, Boolean autoIncrement, String dataTypeName, String precision, String additionalInformation) {
         // Translate type to database-specific type, if possible
        DataType returnTypeName = null;

        if (dataTypeName.equalsIgnoreCase("TEXT")) {
            returnTypeName = new ClobType("TEXT");
        } else if (dataTypeName.equalsIgnoreCase("CLOB")) {
            returnTypeName = new ClobType("LONGTEXT");
        } else if (columnTypeString.equalsIgnoreCase("timestamp")) {
            //this was already in the original mysqlconverter without the addprecision, so i left it intact
            return new DateTimeType("TIMESTAMP");
        } else {
            return super.getDataType(columnTypeString, autoIncrement,
                    dataTypeName, precision, additionalInformation);
        }

        addPrecisionToType(precision, returnTypeName);
        returnTypeName.setAdditionalInformation(additionalInformation);

        return returnTypeName;
    }
}
