/**
 * 
 * Copyright 2010 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of
 * merchantability or fitness for a particular purpose. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.psi.fda.serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import ch.psi.fda.messages.DataMessage;
import ch.psi.fda.messages.EndOfStreamMessage;
import ch.psi.fda.messages.Message;
import ch.psi.fda.messages.Metadata;
import ch.psi.fda.messages.StreamDelimiterMessage;
import ch.psi.pshell.core.Context;

/**
 * Serialize data received by a DataQueue
 */
public class SerializerTXT {

	private static final Logger logger = Logger.getLogger(SerializerTXT.class.getName());

	private File file;
	private boolean appendSuffix = true;

	private boolean first = true;
	private File outfile;

	private int icount;
	private String basename;
	private String extension;
	private boolean newfile;
	private boolean dataInBetween;
	private BufferedWriter writer;
	private StringBuffer b;
	private StringBuffer b1;
	private boolean showDimensionHeader = true;

	public SerializerTXT(File file) {
		this.file = file;
	}

	/**
	 * @param metadata
	 * @param file
	 * @param appendSuffix
	 *            Flag whether to append a _0000 suffix after the original file
	 *            name
	 */
	public SerializerTXT(File file, boolean appendSuffix) {
		this.file = file;
		this.appendSuffix = appendSuffix;
	}

	@Subscribe
	public void onMessage(Message message) {
		try {
			if (first) {
				first = false;
				// Write header

				icount = 0;
				newfile = true;
				dataInBetween = false;
				writer = null;

				// Get basename of the file
				basename = this.file.getAbsolutePath(); // Determine file name
				extension = basename.replaceAll("^.*\\.", ""); // Determine
																// extension
				basename = basename.replaceAll("\\." + extension + "$", "");
			}

			if (message instanceof DataMessage) {
				dataInBetween = true;
				if (newfile) {

					b = new StringBuffer();
					b1 = new StringBuffer();
					b.append("#");
					b1.append("#");
					for (Metadata c : ((DataMessage) message).getMetadata()) {

						b.append(c.getId());
						b.append("\t");

						b1.append(c.getDimension());
						b1.append("\t");
					}
					b.setCharAt(b.length() - 1, '\n');
					b1.setCharAt(b1.length() - 1, '\n');

					// Open new file and write header
					// Construct file name
					if (appendSuffix) {
						outfile = new File(String.format("%s_%04d.%s", basename, icount, extension));
					}
					else {
						outfile = new File(String.format("%s.%s", basename, extension));
					}

					// Open file
					logger.fine("Open new data file: " + outfile.getAbsolutePath());
					writer = new BufferedWriter(new FileWriter(outfile));

					// Write header
					writer.write(b.toString());
					if (showDimensionHeader) {
						writer.write(b1.toString());
					}

					newfile = false;
                                         if (Context.getInstance()!=null){
                                             Context.getInstance().addDetachedFileToSession(outfile);
                                         }
				}

				// Write message to file - each message will result in one line
				DataMessage m = (DataMessage) message;
				StringBuffer buffer = new StringBuffer();
				for (Object o : m.getData()) {
					if (o.getClass().isArray()) {
						// If the array object is of type double[] display its
						// content
						if (o instanceof double[]) {
							double[] oa = (double[]) o;
							for (double o1 : oa) {
								buffer.append(o1);
								buffer.append(" "); // Use space instead of tab
							}
							buffer.replace(buffer.length() - 1, buffer.length() - 1, "\t"); // Replace
																							// last
																							// space
																							// with
																							// tab
						}
						else if (o instanceof Object[]) {
							// TODO need to be recursive ...
							Object[] oa = (Object[]) o;
							for (Object o1 : oa) {
								buffer.append(o1);
								buffer.append(" "); // Use space instead of tab
							}
							buffer.replace(buffer.length() - 1, buffer.length() - 1, "\t"); // Replace
																							// last
																							// space
																							// with
																							// tab
						}
						else {
							buffer.append("-"); // Not supported
						}
					}
					else {
						buffer.append(o);
						buffer.append("\t");
					}
				}

				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1); // Remove last
																// character
																// (i.e. \t)
					buffer.append("\n"); // Append newline
				}
				writer.write(buffer.toString());
			}
			else if (message instanceof StreamDelimiterMessage) {
				StreamDelimiterMessage m = (StreamDelimiterMessage) message;
				logger.fine("Delimiter - number: " + m.getNumber() + " iflag: " + m.isIflag());
				if (m.isIflag() && appendSuffix) {
					// Only increase iflag counter if there was data in between
					// subsequent StreamDelimiterMessages.
					if (dataInBetween) {
						icount++;
					}
					dataInBetween = false;

					// Set flag to open new file
					newfile = true;

					// Close file
					writer.close();
				}
			}
			else if (message instanceof EndOfStreamMessage) {
				if (writer != null) {
					// Close file
					writer.close(); // If the stream was closed previously this
									// has no effect
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Data serializer had a problem writing to the specified file", e);
			throw new RuntimeException("Data serializer had a problem writing to the specified file", e);
		}
	}

	public boolean isShowDimensionHeader() {
		return showDimensionHeader;
	}

	public void setShowDimensionHeader(boolean showDimensionHeader) {
		this.showDimensionHeader = showDimensionHeader;
	}
}
