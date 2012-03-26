package edu.cmu.scs.fluorite.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.w3c.dom.Element;

import edu.cmu.scs.fluorite.model.EventRecorder;
import edu.cmu.scs.fluorite.model.FileSnapshotManager;
import edu.cmu.scs.fluorite.util.Utilities;

public class FileOpenCommand extends BaseDocumentChangeEvent {

	public FileOpenCommand(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			try {
				IFileEditorInput fileInput = (IFileEditorInput) input;
				IFile file = fileInput.getFile();
				IProject project = file.getProject();
				mProjectName = project.getName();
				mFilePath = fileInput.getFile().getLocation().toOSString();

				String content = Utilities.getDocument(editor).get();
				calcNumericalValues(content);

				// Snapshot
				if (!FileSnapshotManager.getInstance().isSame(mFilePath,
						content)) {
					mSnapshot = content;
					FileSnapshotManager.getInstance().updateSnapshot(mFilePath,
							content);
				} else {
					mSnapshot = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String mFilePath;
	private String mProjectName;
	private String mSnapshot;

	public boolean execute(IEditorPart target) {
		// Not supported yet

		// IWorkbenchWindow window =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		// if (window == null) { return false; }
		//
		// IWorkbenchPage page = window.getActivePage();
		// IEditorReference[] editorReferences = page.getEditorReferences();
		return false;
	}

	public void dump() {
		// TODO Auto-generated method stub

	}

	public Map<String, String> getAttributesMap() {
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("projectName", mProjectName == null ? "null" : mProjectName);

		Map<String, Integer> numericalValues = getNumericalValues();
		if (numericalValues != null) {
			for (Map.Entry<String, Integer> pair : numericalValues.entrySet()) {
				attrMap.put(pair.getKey(), Integer.toString(pair.getValue()));
			}
		}

		return attrMap;
	}

	public Map<String, String> getDataMap() {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("filePath", mFilePath == null ? "null" : mFilePath);
		if (mSnapshot != null) {
			dataMap.put("snapshot", mSnapshot);
		}

		return dataMap;
	}

	public String getCommandType() {
		return "FileOpenCommand";
	}

	public AbstractCommand createFrom(Element commandElement) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return "File Open: \"" + mFilePath + "\"";
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() {
		return EventRecorder.MacroCommandCategory;
	}

	public String getCategoryID() {
		return EventRecorder.MacroCommandCategoryID;
	}

	public boolean combine(ICommand anotherCommand) {
		return false;
	}
}