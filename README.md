# oracle-xlsx-code-generator

Based on the package <b>AS_XLSX</b>: https://github.com/sokolsaiti/as_xlsx/blob/master/as_xlsx.sql

Actual version: https://github.com/mrprogre/oracle_xlsx/raw/master/oracle_xlsx.jar

Java must be installed: https://www.java.com/ru/download/

info: https://habr.com/ru/post/567966/

![image](https://user-images.githubusercontent.com/45883640/200116204-dd09ca46-81e9-48af-a7af-5d0ec9c61780.png)

Program features:

- PL/SQL object code is generated after entering parameters and columns by clicking on the "Create objects" button;

- Columns can be edited manually or loaded using the "Open file" button from CSV or TXT files.

Example CSV file content
    
    ID;14;BD_ID;NUMBER;
    NAME;20;BD_NAME;VARCHAR2(60);
    DATE;18;DB_DATE;DATE

Record is created to choose from 2 options:
    - based on %TYPE view or table (not %ROWTYPE because more often not all fields are needed for unloading);
    - based on the entered types in the Columns table.

- Ability to clear the list of parameters and columns;

- Added comboboxes with fonts, data types, boolean variables and cell alignment;

- The background color of the headers can be seen immediately in the options. When specifying a different color code, the cell background will change the color to the specified one;

- Columns with data type DATE will be cast to type 'DD.MM.YYYY'.

- Fixed header line

After clicking on the "Create objects" button, this code is generated:

    -- Package specification
    -- version 1
    type t_type_of_record is record (
        BD_ID table_or_view.BD_ID%TYPE,
        BD_NAME table_or_view.BD_NAME%TYPE,
        DB_DATE table_or_view.DB_DATE%TYPE
    );

    -- version 2
    type t_type_of_record is record (
        BD_ID NUMBER,
        BD_NAME VARCHAR2(60),
        DB_DATE DATE
    );

    type t_type_of_record_tbl is table of t_type_of_record;

    procedure get_xls_from_table;
    function get_xls_function(p_type in t_type_of_record_tbl) return blob;

    -- Package body
    -- procedure
    procedure get_xls_from_table is
      l_file BLOB;
      l_file_name varchar2(30) := 'file_name';
      l_type t_type_of_record_tbl;
    begin
      select BD_ID, BD_NAME, DB_DATE 
        bulk collect into l_type
        from table_or_view
       where column_name between sysdate and sysdate + 1;

      l_file := get_xls_function(l_type);

      if lengthb(l_file) > 0 then
        api_datasource.setColumnValue('BLOB_DUAL_ds.blob', l_file);
        api_datasource.download('BLOB_DUAL_ds.blob', l_file_name||'.xlsx');
      end if;

    exception
      when others then
        sb_util.write_log('package.get_xls_from_table ошибка: ' || sqlerrm ||chr(13)||
    dbms_utility.format_error_backtrace, 'info');
    end;

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
        as_xlsx.set_column_width(1, 14); as_xlsx.cell(1, 1, 'ID', p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));
        as_xlsx.set_column_width(2, 20); as_xlsx.cell(2, 1, 'NAME', p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));
        as_xlsx.set_column_width(3, 18); as_xlsx.cell(3, 1, 'DATE', p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true));
      as_xlsx.freeze_rows(1);

    FOR i IN 1..p_type.count
        loop
        row_num := row_num + 1;
        as_xlsx.set_row(row_num, p_fontId => as_xlsx.get_font('Times New Roman', p_fontsize => 12), 
    p_borderId => as_xlsx.get_border);
        as_xlsx.set_row_height(row_num, 25);
        --
        as_xlsx.cell(1, i + 1, coalesce(p_type(i).BD_ID, '-'), p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => 
    as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        as_xlsx.cell(2, i + 1, coalesce(p_type(i).BD_NAME, '-'), p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => 
    as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        as_xlsx.cell(3, i + 1, coalesce(to_char(p_type(i).DB_DATE, 'dd.mm.yyyy'), '-'), p_alignment => as_xlsx.get_alignment
    (p_horizontal => 'center', p_vertical => 'center', p_wraptext => true), p_fontId => 
    as_xlsx.get_font('Times New Roman', p_fontsize => 12, p_bold => false, p_italic => false));
        end loop;
      v_file := as_xlsx.finish;
      RETURN v_file;
      exception
       when others then
        sb_util.write_log('package.get_xls_function ошибка: ' ||sqlerrm||chr(13)||
    dbms_utility.format_error_backtrace, 'info'); 
    END;
    
Result:

![image](https://user-images.githubusercontent.com/45883640/200116194-ef0c3562-cb40-42f6-9cc4-c11216d22170.png)
