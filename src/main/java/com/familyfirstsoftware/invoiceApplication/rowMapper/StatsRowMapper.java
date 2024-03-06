package com.familyfirstsoftware.invoiceApplication.rowMapper;

import com.familyfirstsoftware.invoiceApplication.domain.Role;
import com.familyfirstsoftware.invoiceApplication.domain.Stats;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/*
    * This class is used to map the result of a query to a Role object
    SELECT c.total_customers, i.total_invoices, inv.total_billed FROM
    *       (SELECT COUNT(*) total_customers FROM customer) c,  // total_customers
    *       (SELECT COUNT(*) total_invoices FROM invoice) i,   // total_invoices
    *       (SELECT ROUND(SUM(total)) total_billed FROM invoice) inv;  // total_billed
 */
public class StatsRowMapper implements RowMapper<Stats> {

    @Override
    public Stats mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Stats.builder()
                .totalCustomers(resultSet.getInt("total_customers"))
                .totalInvoices(resultSet.getInt("total_invoices"))
                .totalBilled(resultSet.getDouble("total_billed"))
                .build();
    }
}
