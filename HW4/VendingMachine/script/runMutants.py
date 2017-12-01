#!/usr/bin/env python3

import os
import sys
import shutil as shu
import subprocess as sub
import traceback as tb

def main():
    # Collect the mujava mutants...
    muDir = sys.argv[1]
    srcDir = sys.argv[2]
    binDir = srcDir.replace('src/', 'bin/')
    cpDir = binDir[0:binDir.rfind('/')]

    # Set CLASSPATH
    #CLASSPATH=/usr/share/java/junit4-4.12.jar:/home/edavis/Work/AdvSWEng/Assignments/HW4/VendingMachine/bin
    os.environ['CLASSPATH'] = '/usr/share/java/junit4-4.12.jar:%s' % cpDir

    testCmd = 'java test.TestRunner'
    testPass = '9/9 tests passed'

    for (root, dirs, files) in os.walk(muDir):
        for dir in dirs:
            subdir = os.path.join(root, dir)
            for (subroot, subdirs, files) in os.walk(subdir):
                for file in files:
                    if file.endswith('.java'):
                        muFile = os.path.join(subroot, file)
                        elems = muFile.split('/')
                        method = elems[-3]
                        muName = elems[-2]
                        fileName = elems[-1]
                        #print(muFile)
                        srcFile = '%s/%s' % (srcDir, fileName)
                        shu.copyfile(muFile, srcFile)

                        print("Compiling mutant: '" + muName + "' in method '" + method + "'")
                        compCmd = '/usr/bin/javac %s' % srcFile
                        print(compCmd)
                        bytes = sub.check_output(compCmd.split(' '), env=os.environ, stderr=sub.STDOUT)
                        output = bytes.decode()

                        if len(output) > 0:
                            # Compiler error:
                            print(output)
                            return
                        else:
                            # Copy the class file
                            srcClass = srcFile.replace('.java', '.class')
                            binClass = srcClass.replace('src/', 'bin/')
                            print("mv " + srcClass + " " + binClass)
                            shu.move(srcClass, binClass)

                            # Run the JUnit tests
                            print(testCmd)
                            bytes = sub.check_output(testCmd.split(' '), env=os.environ, stderr=sub.STDOUT)
                            output = bytes.decode()
                            if output.startswith(testPass):
                                # All tests pass, the mutant lives!
                                print("Mutant '" + muName + "' lives!")
                            else:
                                print(output)



    pass

try:
    main()
except KeyboardInterrupt as e: # Ctrl-C
    print("Closing gracefully on keyboard interrupt...")
except Exception as e:
    print('ERROR: ' + str(e))
    tb.print_exc()
