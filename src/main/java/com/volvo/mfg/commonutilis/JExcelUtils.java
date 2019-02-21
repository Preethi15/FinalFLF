package com.volvo.mfg.commonutilis;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.testng.Assert;
import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class JExcelUtils {
	
//	 private static final Logger logger = Logger.getLogger(ExcelUtil.class);
		private WorkbookSettings workBookSettings;
		private Workbook workBook;
		private Sheet sheet;
		private List<String> sheetNames;
		private String workBookName;

		/**
		 * Constructor for the ExcelUtil
		 */
		public JExcelUtils(final String filePath)
		{
			workBookSettings = new WorkbookSettings();
			workBookSettings.setLocale(new Locale("en", "EN"));
			try
			{
				File file = new File(filePath);
				workBookName = file.getName();
				workBook = Workbook.getWorkbook(file, workBookSettings);
				sheetNames = Arrays.asList(workBook.getSheetNames());
				for(String s:sheetNames) {
//					System.out.println(s);
				}
			}
			catch (Exception e)
			{
				Assert.fail(e.getMessage());
			}
		}

		/**
		 * Returns the specified column content as a string array.
		 *
		 * @param columnIndex
		 *            Index of the column.
		 *
		 * @return <li><code>String []</code> content of the column specified.</li>.
		 */
		public String[] getColumnContent(final int columnIndex)
		{
			Cell[] columnArray;
			String[] columnContent;
			columnArray = sheet.getColumn(columnIndex - 1);
			columnContent = new String[columnArray.length];
			for (int index = 0; index < columnArray.length; index++)
			{
				columnContent[index] = columnArray[index].getContents().toString();
			}
			return columnContent;
		}

		/**
		 * Returns the specified row content as a string array.
		 *
		 * @param rowIndex
		 *            Index of the row.
		 * @return <li><code>String []</code> content of the row specified.</li>.
		 */
		public String[] getRowContent(final int rowIndex)
		{
			Cell[] cellArray;
			String[] rowContent;
			try
			{
				cellArray = sheet.getRow(rowIndex - 1);
				rowContent = new String[cellArray.length];
				for (int index = 0; index < cellArray.length; index++)
				{
					rowContent[index] = cellArray[index].getContents().toString();
				}
				return rowContent;

			}
			catch (Exception e)
			{
				Assert.fail(e.getMessage());
			}
			return null;
		}
		
		public HashMap<String,String> getDataMap (final String sheetName)
		{
			Cell[] cellArray;
			String value, key = null;
			HashMap<String,String> dataMap = new HashMap<String,String>();
			try
			{
				this.setSheet(sheetName);
				String[] columnNames = getRowContent(1);
				//int rowIndex = this.getRowIndex(sheetName);
				int rowIndex =2;
				cellArray = sheet.getRow(rowIndex - 1);
				for (int index = 0; index < columnNames.length; index++)
				{
					value = cellArray[index].getContents().toString();
					key = columnNames[index];
					dataMap.put(key, value);
					
				}
				return dataMap;

			}
			catch (Exception e)
			{
				Assert.fail(e.getMessage());
			}
			return null;		}
		

		/**
		 * Returns the specified cell content based on row & column index.
		 *
		 * @param columnIndex
		 *            Column index of the cell.
		 * @param rowIndex
		 *            Row index of the cell.
		 * @return <li><code>String</code> content of specified cell.</li>
		 */
		public String getCellContent(final int columnIndex, final int rowIndex)
		{
			String value = null;
			Cell cell;
			cell = sheet.getCell(columnIndex - 1, rowIndex - 1);
			value = cell.getContents().toString();
			return value;
		}

		/**
		 * Returns the index position of the column specified.
		 *
		 * @param columnName
		 *            Name of the column.
		 * @return <li><code>int</code>index position of the column specified.</li>
		 *
		 */
		/*
		 * public int getColumnIndex(final String columnName) { String[] columnNames
		 * = getRowContent(1); int position = 0; try { for (int index = 0; index <
		 * columnNames.length; index++) { if
		 * (columnNames[index].equalsIgnoreCase(columnName)) { position = index + 1;
		 * break; } } return position; } catch(Exception e) { throw new
		 * IllegalArgumentException("The specified column name " + columnName +
		 * " doesnt exist in the current workbook."); } }
		 */

		public int getColumnIndex(final String columnName)
		{
			String[] columnNames = getRowContent(1);
			int position = 0;

			for (int index = 0; index < columnNames.length; index++)
			{
				if(columnNames[index].equalsIgnoreCase(columnName))
				{
					position = index + 1;
					return position;
				}
			}
			if(position == 0)
			{
				Assert.fail("The specified column name " + columnName
						+ " doesnt exist in the Workbook Sheet - " + sheet.getName() + ".");
			}
			return 0;		}

		//Added by me
		public boolean isColumnExists(final String columnName)
		{
			String[] columnNames = getRowContent(1);

			for (int index = 0; index < columnNames.length; index++)
			{
				if(columnNames[index].equalsIgnoreCase(columnName))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns the Cell content matching the specified Test Case ID (where the
		 * Test Case ID is part of the merged cell), iteration number & column
		 * Position.
		 *
		 * @param testCase
		 * 			Test case id as String.
		 * @param Occurance
		 * 			Iteration number.
		 * @param columnPosition
		 * 			Column position.
		 * @return <String> cell content matching the test case id.
		 */
		public String getDataFromMergedCell(String testCase, String Occurance, int columnPosition)
		{
			if(this.isMergedCell(testCase))
			{
				Range[] cellRange = sheet.getMergedCells();
				for (Range range : cellRange)
				{
					if((range.getTopLeft().getContents().toString()).equalsIgnoreCase(testCase))
					{
						int startIndex = range.getTopLeft().getRow();
						int endIndex = range.getBottomRight().getRow();
						for (int index = startIndex; index <= endIndex; index++)
						{
							if(this.getCellContent(2, index + 1).equalsIgnoreCase(Occurance))
							{
								return this.getCellContent(columnPosition, index + 1);
							}
						}
					}
				}
			}
			return null;
		}

		/**
		 * Returns the specified iterator cell index of the Merged Cell.
		 *
		 * @param testCase
		 *            Test Case ID as string.
		 * @param occurance
		 *            Iteration number.
		 * @return the specified iterator row index.
		 */
		public int getMergedCellRowIndex(String testCase, String occurance)
		{
			if(this.isMergedCell(testCase))
			{
				Range[] cellRange = sheet.getMergedCells();
				for (Range range : cellRange)
				{
					if((range.getTopLeft().getContents().toString()).equalsIgnoreCase(testCase))
					{
						int startIndex = range.getTopLeft().getRow();
						int endIndex = range.getBottomRight().getRow();
						for (int index = startIndex; index <= endIndex; index++)
						{
							String value = this.getCellContent(2, index + 1);
							if(value.equalsIgnoreCase(occurance))
							{
								return index + 1;
							}
						}
					}
				}
			}
			return 0;
		}

		/**
		 * Returns the number of rows in the specified Sheet.
		 *
		 * @return number of rows in the Sheet.
		 */
		public int getRowCount()
		{
			int count = 0;
			int totalCount = sheet.getRows();
			for (int index = 1; index <= totalCount; index++)
			{
				Cell[] cell = sheet.getRow(index - 1);
				if(cell[0].getContents().toString().isEmpty()
						&& cell[1].getContents().toString().isEmpty())
				{
					count++;
				}
			}
			return totalCount - count;
		}

		/**
		 * Returns the number of column in the specified Sheet.
		 *
		 * @return number of column in the Sheet.
		 */
		public int getColumnCount()
		{
			return sheet.getColumns();
		}

		/**
		 * Returns the row index for the text specified.
		 *
		 * @param cellContent
		 *            Cell content.
		 * @return row index position for the text specified.
		 */
		public int getRowIndex(String cellContent)
		{
			try
			{
				return sheet.findCell(cellContent).getRow();
			}
			catch (NullPointerException e)
			{
				Assert.fail("The Specified Cell Content " + cellContent
						+ " doesnt exist in the "+workBookName+" workbook.");
				return -1;
			}
		}
		/**
		 * Close the Work Book session.
		 */
		public void close()
		{
			try
			{
				workBook.close();
			}
			catch (Exception e)
			{
				Assert.fail(e.getMessage());
			}
		}

		/**
		 * Ensures whether the specified content is part of a merged cell or not.
		 * @param cellContent
		 *            Cell content to be validated.
		 * @return <li><code>true</code> is the specified cell content is part of
		 *         merged cell.</li><li><code>false</code> Else otherwise.</li>
		 */
		public boolean isMergedCell(String cellContent)
		{
			Range[] range = sheet.getMergedCells();
			for (Range cell : range)
			{
				if((cell.getTopLeft().getContents().toString()).equalsIgnoreCase(cellContent))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * Sets the specified sheet as active sheet in the work book.
		 *
		 * @param sheetName
		 *            Sheet to be accessed.
		 */
		public void setSheet(final String sheetName)
		{
			if(sheetNames.contains(sheetName))
			{
				sheet = workBook.getSheet(sheetName);
			}
			else
			{
				Assert.fail(sheetName+" sheet is not found in "+ workBookName);
			}
		}

}
