package QueriesCreater;

public class GetSqlQuery {

    //формирование функции
    String getQueries(Object [][] pParameters, Object [][] pColumns) {
        Object functionName = pParameters[0][1];
        Object procedureName = pParameters[1][1];
        Object recordName = pParameters[2][1];
        Object typeTableName = pParameters[3][1];
        Object tableOrViewName = pParameters[4][1];
        Object headersBackground = pParameters[5][1];
        Object headersFontSize = pParameters[6][1];
        Object rowsFontSize = pParameters[7][1];
        Object rowHeight = pParameters[8][1];
        Object rowsBold = pParameters[9][1];
        Object rowsItalic = pParameters[10][1];
        Object xlsFont = pParameters[11][1];
        Object rowsFont = pParameters[12][1];
        Object horizontalAlignment = pParameters[13][1];
        Object verticalAlignment = pParameters[14][1];
        Object wrapText = pParameters[15][1];

        StringBuilder xlsHeaders = new StringBuilder("-- Package specification\n");
        // создаём запись (1 вариант)
        xlsHeaders.append("-- version 1\n");
        xlsHeaders.append("type ").append(recordName).append(" is record (\n");
        for (Object[] column : pColumns) {
            xlsHeaders.append("\t").append(column[2]).append(" ").append(tableOrViewName).append(".").append(column[2]).append("%TYPE,\n");
        }
        // удаляем крайнюю запятую
        xlsHeaders.deleteCharAt(xlsHeaders.length() - 2);
        xlsHeaders.append(");\n\n");

        // создаём запись (2 вариант)
        xlsHeaders.append("-- version 2\n");
        xlsHeaders.append("type ").append(recordName).append(" is record (\n");
        for (Object[] column : pColumns) {
            xlsHeaders.append("\t").append(column[2]).append(" ").append(column[3]).append(",\n");
        }
        // удаляем крайнюю запятую
        xlsHeaders.deleteCharAt(xlsHeaders.length() - 2);
        xlsHeaders.append(");\n\n");

        //создаём таблицу на основе записи
        xlsHeaders.append("type ").append(typeTableName).append(" is table of ").append(recordName).append(";\n\n");
        // создаём функцию на основе типа таблицы
        xlsHeaders.append("function ").append(functionName).append("(p_type in ").append(typeTableName).append(") return blob;\n\n");

        // тело пакета
        xlsHeaders.append("-- Package body\n");
        // Создание процедуры для скачивания файла
        xlsHeaders.append("-- procedure\n");
        xlsHeaders.append("procedure ").append(procedureName).append(" is\n")
                .append("  l_file BLOB;\n")
                .append("  l_file_name varchar2(30) := 'file_name';\n")
                .append("  l_type ").append(typeTableName).append(";\n")
                .append("begin\n")
                .append("  select ");
        for (Object[] column : pColumns) {
            xlsHeaders.append(column[2]).append(", ");
        }
        xlsHeaders.deleteCharAt(xlsHeaders.length() - 2).append("\n");
        xlsHeaders.append("    bulk collect into l_type\n    from ")
                .append(tableOrViewName).append("\n");
        xlsHeaders.append("   where column_name between sysdate and sysdate + 1;\n" + "\n" + "  l_file := ")
                .append(functionName).append("(l_type);\n").append("\n")
                .append("  if lengthb(l_file) > 0 then\n")
                .append("    api_datasource.setColumnValue('BLOB_DUAL_ds.blob', l_file);\n")
                .append("    api_datasource.download('BLOB_DUAL_ds.blob', l_file_name||'.xlsx');\n")
                .append("  end if;\n\n")
                .append("exception\n" + "  when others then\n" + "    sb_util.write_log('package.")
                .append(procedureName)
                .append(" ошибка: ' || sqlerrm ||chr(13)||\ndbms_utility.format_error_backtrace, 'info');\n")
                .append("end;");

        // Создание функции для формирования файла
        xlsHeaders.append("-- function\n");
        xlsHeaders.append("function ").append(functionName).append("(p_type in ").append(typeTableName).append(") return blob is\n")
                .append("  row_num number := 0;\n")
                .append("  v_file  blob;\n")
                .append("begin\n")
                .append("  as_xlsx.clear_workbook;\n")
                .append("  as_xlsx.new_sheet('tab1');\n")
                .append("  row_num := row_num + 1;\n")
                .append("  as_xlsx.set_row(row_num, p_fontId => as_xlsx.get_font('")
                .append(xlsFont)
                .append("', p_fontsize => ")
                .append(headersFontSize)
                .append(", p_bold => true),\n")
                .append("  p_fillId => as_xlsx.get_fill('solid', '")
                .append(headersBackground)
                .append("'), p_borderId => as_xlsx.get_border);\n")
                .append("\t-- headers\n")
                .append("\tas_xlsx.set_row_height(1, ")
                .append(rowHeight)
                .append(");\n");

        // headers
        for (int i = 0; i < pColumns.length; i++) {
            int x = i + 1;
            xlsHeaders.append("\tas_xlsx.set_column_width(")
                    .append(x).append(", ").append(pColumns[i][1]).append("); ")
                    .append("as_xlsx.cell(").append(x).append(", 1, '").append(pColumns[i][0]).append("', ")
                    .append("p_alignment => as_xlsx.get_alignment\n")
                    .append("(p_horizontal => '").append(horizontalAlignment).append("', ")
                    .append("p_vertical => '").append(verticalAlignment).append("', ")
                    .append("p_wraptext => ").append(wrapText)
                    .append("));\n");
        }
        xlsHeaders.append("  \nFOR i IN 1..p_type.count\n" + "    loop\n"
                + "    row_num := row_num + 1;\n"
                + "    as_xlsx.set_row(row_num, p_fontId => as_xlsx.get_font('")
                .append(xlsFont)
                .append("', p_fontsize => ").append(rowsFontSize)
                .append("), \np_borderId => as_xlsx.get_border);\n")
                .append("    as_xlsx.set_row_height(row_num, ")
                .append(rowHeight)
                .append(");\n    --\n");

        // rows
        for (int i = 0; i < pColumns.length; i++) {
            int x = i + 1;
            xlsHeaders.append("    as_xlsx.cell(")
                    .append(x).append(", i + 1, coalesce(p_type(i).").append(pColumns[i][2]).append(", '-'), ")
                    .append("p_alignment => as_xlsx.get_alignment\n")
                    .append("(p_horizontal => '").append(horizontalAlignment).append("', ")
                    .append("p_vertical => '").append(verticalAlignment).append("', ")
                    .append("p_wraptext => ").append(wrapText)
                    .append("), p_fontId => \nas_xlsx.get_font('")
                    .append(rowsFont)
                    .append("', p_fontsize => ")
                    .append(rowsFontSize)
                    .append(", p_bold => ")
                    .append(rowsBold)
                    .append(", p_italic => ")
                    .append(rowsItalic)
                    .append("));\n");
        }
        xlsHeaders.append("    end loop;\n"
                + "  v_file := as_xlsx.finish;\n"
                + "  RETURN v_file;\n"
                + "  exception\n"
                + "   when others then\n"
                + "    sb_util.write_log('package.")
                .append(functionName)
                .append(" ошибка: ' ||sqlerrm||chr(13)||\ndbms_utility.format_error_backtrace, 'info'); \n")
                .append("END;\n\n");

        return xlsHeaders.toString();
    }
}
