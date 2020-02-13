package com.haozhi.item.dao;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class PersonDynamicSqlProvider {
    public String select(Map<String, Object> map) {
        return new SQL() {
            {
                SELECT("*");
                FROM("haozhi_user");

                StringBuilder whereClause = new StringBuilder();
                if (map.get("oneId") != null && !("").equals(map.get("oneId"))) {
                    whereClause.append(" and super_id = ").append(map.get("oneId"));
                }
                if (map.get("date1") != null && !("").equals(map.get("date1"))) {
                    whereClause.append(" and time >= ").append("'").append(map.get("date1")).append("'");
                }
                if (map.get("date2") != null && !("").equals(map.get("date2"))) {
                    whereClause.append(" and time <= ").append("'").append(map.get("date2")).append("'");
                }
                if (map.get("name") != null && !("").equals(map.get("name"))) {
                    whereClause.append(" and name like '%").append(map.get("name")).append("%' ");
                }
                if (!"".equals(whereClause.toString())) {
                    WHERE(whereClause.toString().replaceFirst("and", ""));
                }
            }
        }.toString();
    }

}
