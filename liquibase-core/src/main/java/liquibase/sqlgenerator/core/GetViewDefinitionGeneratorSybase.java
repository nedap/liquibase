package liquibase.sqlgenerator.core;

import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.database.core.SybaseDatabase;
import liquibase.structure.core.Schema;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.GetViewDefinitionStatement;

public class GetViewDefinitionGeneratorSybase extends GetViewDefinitionGenerator {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(GetViewDefinitionStatement statement, Database database) {
        return database instanceof SybaseDatabase;
    }

    @Override
    public Sql[] generateSql(GetViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        CatalogAndSchema schema = database.correctSchema(new CatalogAndSchema(statement.getCatalogName(), statement.getSchemaName()));

        String sql = "select text from syscomments where id = object_id('" +
                schema.getSchemaName() + "." +
                statement.getViewName() + "') order by colid";

        return new Sql[]{
                new UnparsedSql(sql)
        };
    }
}