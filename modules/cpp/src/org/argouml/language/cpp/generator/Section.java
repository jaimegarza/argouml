// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.language.cpp.generator;

/**
 * Reading and writing preserved sections from the code.
 *
 * @since 24. Februar 2002, 15:30
 */
public class Section extends AbstractSection {

    /**
     * @see org.argouml.uml.generator.AbstractSection#generate(
     * java.lang.String, java.lang.String)
     */
    public static String generate(String id, String indent) {
        return (generateTop(id, indent) + generateBottom(id, indent));
    }

    /**
     * Variant of generate( String id, String indent ) which allows
     * parted generation of Top and Bottom, so that GeneratorXX.java
     * can place an initial body within the both identifying lines.
     *
     * @param id the section id
     * @param indent the current indentation
     * @return the generated string
     */
    public static String generateTop(String id, String indent) {
        String s =
            indent + "// don't delete the following line as "
            + "it's needed to preserve source code of this";
        s += " autogenerated element\n";
        s += indent + "// section " + id + " begin\n";
        return s;
    }

    /**
     * Variant of generate( String id, String indent ) which allows
     * parted generation of Top and Bottom, so that GeneratorXX.java
     * can place an initial body within the both identifying lines.
     *
     * @param id the section id
     * @param indent the current indentation
     * @return the generated string
     */
    public static String generateBottom(String id, String indent) {
        String s = "";
        s += indent + "// section " + id + " end\n";
        s += indent + "// don't delete the previous line as it's needed "
            + "to preserve source code of this";
        s += " autogenerated element\n";
        return s;
    }
}
