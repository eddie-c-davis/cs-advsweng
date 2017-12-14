#!/mu/bin/perl

#####################################################################################################
#                                                                                                   #
# Copyright 2009-2010 Micron Technology, Inc.                                                       #
#                                                                                                   #
# AUTHOR: edavis                                                                                    #
# NAME:   xls2csv.pl                                                                                #
# DESC:   Converts Excel workbook to CSV files.                                                     #
#                                                                                                   #
#####################################################################################################

use warnings;
use strict;

use Spreadsheet::ParseExcel;


my $wbPath = $ARGV[0];

if (!defined($wbPath))
{
    print STDOUT "usage: xls2csv excel_book.xls\n";
    exit();
}

foreach $wbPath (@ARGV)
{
    if (-e $wbPath)
    {    
        my $csvDir = '.';
    
        # Extract directory from WB path    
        my $index = rindex($wbPath, '/');
        if ($index >= 0)
        {
            $csvDir = substr($wbPath, 0, $index);
        }
    
        $csvDir = sprintf("%s/csv", $csvDir);
        if (!(-d $csvDir))
        {
            mkdir($csvDir, 0777);
        }
        
        print STDOUT sprintf("Opening workbook '%s'...\n", $wbPath);   
        my $book = Spreadsheet::ParseExcel::Workbook->Parse($wbPath);
        
        foreach my $sheet (@{$book->{Worksheet}})
        {        
            if (defined($sheet->{MinRow}) && defined($sheet->{MaxRow}) &&
                defined($sheet->{MinCol}) && defined($sheet->{MaxCol}))
            {
                my $csvFile = sprintf("%s/%s.csv", $csvDir, $sheet->{Name});            
                
                if (open(CSV, ">$csvFile"))
                {
                    print STDOUT sprintf("Writing CSV file '%s'...\n", $csvFile);
                    
                    my $line;
                    for (my $row = $sheet->{MinRow}; $row <= $sheet->{MaxRow} ; $row++)
                    {                
                        $line = '';                    
                        for(my $col = $sheet->{MinCol}; $col <= $sheet->{MaxCol}; $col++)
                        {
                            my $cell = $sheet->{Cells}[$row][$col];
        
                            my $cellValue = '';
                            if (defined($cell) && defined($cell->Value))
                            {
                                $cellValue = $cell->Value;
                            }
        
                            $line = sprintf("%s%s,", $line, $cellValue);
                        }
    
                        # Remove trailing comma
                        chop($line);
    
                        # Write to file                    
                        print CSV sprintf("%s\n", $line);
                    }
    
                    close(CSV);
                }
                else
                {
                    die("ERROR: Cannot open '$csvFile' for writing... $!\n");
                }
            }        
        }

        print STDOUT "\n";
    }
    else
    {
        print STDOUT sprintf("ERROR: File '%s' not found!\n", $wbPath);
    }
}
