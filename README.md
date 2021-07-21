# oracle_xlsx

Export to Excel based on the package AS_XLSX:

https://github.com/sokolsaiti/as_xlsx/blob/master/as_xlsx.sql

gui:

![Image alt](https://github.com/mrprogre/oracle_xlsx/blob/master/GUI.png)

column data entered:

![Image alt](https://github.com/mrprogre/oracle_xlsx/blob/master/gui_2.png)

result:

![Image alt](https://github.com/mrprogre/oracle_xlsx/blob/master/result.png)

    -- Package specification
    -- version 1
    type t_type_of_record is record (
      DB_ID table_or_view.DB_ID%TYPE,
      DB_NAME table_or_view.DB_NAME%TYPE,
      DB_DATE table_or_view.DB_DATE%TYPE
    );

    -- version 2
    type t_type_of_record is record (
      DB_ID NUMBER,
      DB_NAME VARCHAR2(69),
      DB_DATE DATE
    );

    type t_type_of_record_tbl is table of t_type_of_record;

    function get_xls_function(p_type in t_type_of_record_tbl) return blob;

    -- Package body
    -- function
    function get_xls_function(p_type in t_type_of_record_tbl) return blob is
      row_num number := 0;
      v_file  blob;
    begin
      as_xlsx.clear_workbook;
      as_xlsx.new_sheet('tab1');
      row_num := row_num + 1;
      as_xlsx.set_row(row_num, p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 13, p_bold => true),
      p_fillId => as_xlsx.get_fill('solid', 'FFCC66'), p_borderId => as_xlsx.get_border);
      -- headers
      as_xlsx.set_row_height(1, 25);
      as_xlsx.set_column_width(1, 20); as_xlsx.cell(1, 1, 'id', p_alignment => as_xlsx.get_alignment (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));
      as_xlsx.set_column_width(2, 50); as_xlsx.cell(2, 1, 'name', p_alignment => as_xlsx.get_alignment (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));
      as_xlsx.set_column_width(3, 40); as_xlsx.cell(3, 1, 'date', p_alignment => as_xlsx.get_alignment (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));

    FOR i IN 1..p_type.count
        loop
        row_num := row_num + 1;
        as_xlsx.set_row(row_num, p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 12), p_borderId => as_xlsx.get_border);
        as_xlsx.set_row_height(row_num, 25);
        --
        as_xlsx.cell(1, i + 1, coalesce(p_type(i).DB_ID, '-'), p_alignment => as_xlsx.get_alignment(
    p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        as_xlsx.cell(2, i + 1, coalesce(p_type(i).DB_NAME, '-'), p_alignment => as_xlsx.get_alignment(
    p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        as_xlsx.cell(3, i + 1, coalesce(p_type(i).DB_DATE, '-'), p_alignment => as_xlsx.get_alignment(
    p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        end loop;
      v_file := as_xlsx.finish;
      RETURN v_file;
      exception
       when others then
        sb_util.write_log('package.get_xls_function ошибка: ' ||sqlerrm||chr(13)||dbms_utility.format_error_backtrace, 'info'); 
    END;

    -- procedure
    procedure get_xls_from_table is
      l_file BLOB;
      l_type t_type_of_record_tbl;
    begin
      select DB_ID, DB_NAME, DB_DATE 
        bulk collect into l_type
        from table_or_view
       where column_name between sysdate and sysdate + 1;

      l_file := get_xls_function(l_type);

      if lengthb(l_file) > 0 then
        api_datasource.setColumnValue('BLOB_DUAL_ds.blob', l_file);
        api_datasource.download('BLOB_DUAL_ds.blob', file_name||'.xlsx');
      end if;

    exception
      when others then
        sb_util.write_log('package.get_xls_from_table ошибка: ' || sqlerrm ||chr(13)||dbms_utility.format_error_backtrace, 'info');
    end;
