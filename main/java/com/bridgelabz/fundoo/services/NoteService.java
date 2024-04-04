package com.bridgelabz.fundoo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.exception.NoteNotFoundException;
import com.bridgelabz.fundoo.model.Label;
import com.bridgelabz.fundoo.model.Note;
import com.bridgelabz.fundoo.repository.LabelRepository;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.response.Response;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;
    
    @Autowired
	  private LabelRepository labelRepository;
    
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(int id) {
    	Note note = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));
		return note;
    }

    public Response updateNote(int id,Note note) {
    	Note n = noteRepository.findById(id)
	    		.orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));
	    
    	n.setTitle(note.getTitle());
     	n.setDescription(note.getDescription());
	    n.setPinned(note.isPinned()); 
	    n.setArchive(note.isArchive());
	    n.setTrash(note.isTrash());
	    noteRepository.save(n);
	    Response response = new Response();
	    response.setCode(200);
		response.setMessage("Note updated Sucessfully");
        return response;
    }

    public void deleteAllNotes() {	
		noteRepository.deleteAll();
	}
    
    public void deleteNoteById(int id) {
    	Optional<Note> existingNote = noteRepository.findById(id);
		if (!existingNote.isPresent()) {
            throw new NoteNotFoundException("Note with ID " + id + " not found.");
        }
		noteRepository.deleteById(id);
    }
    
    public List<Note> getArchiveNotes() {
        return noteRepository.findArchiveNotes();
     }
	    public List<Note> getPinnedNotes() {
	           return noteRepository.findPinnedNotes();
	        }
	    
	    public List<Note> getTrashNotes() {
       return noteRepository.findTrashNotes();
     }

     public Note setNoteToArchive(int id) {
	       noteRepository.setNoteToArchive(id);
        return null;
     }
     public Note setNoteToPinned(int id) {
	       noteRepository.setNoteToPinned(id);
         return null;
      }


     public Note setNoteToUnArchive(int id) {
         noteRepository.setNoteToUnArchive(id);
         return null;
     }
     
     public Note setNoteToUnPinned(int id) {
         noteRepository.setNoteToUnPinned(id);
         return null;
     }

     public Note setNoteToTrash(int id) {
	        noteRepository.setNoteToTrash(id);
         return null;
     }

     public Note setNoteToUnTrash(int id) {
	        noteRepository.setNoteToUnTrash(id);
         return null;
     }

     public Note addNotesToLabel(int id, String labelName) {

  	    Note notes = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note Data found"));

  	    Label labelModel = labelRepository.findByLabelName(labelName);

  	    if (labelModel == null) {

  	        throw new NoteNotFoundException("Label not found");

  	    }

  	    // Assuming labelModelList is a List<Label> in the Note entity

  	    notes.getLabelModelList().add(labelModel);

  	    return noteRepository.save(notes);

       }


      public List<String> getLabelNamesByNoteId(int id) {
 	        noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Notes not found"));
          List<Integer> labelIdList = noteRepository.findAllLabels(id);
          List<String> labelNameList = new ArrayList<>();
          List<Label> labelModelList = labelRepository.findAllById(labelIdList);
          for (Label labelModel: labelModelList) {
              labelNameList.add(labelModel.getLabelName());
          }
          return labelNameList;
       }


       public Note removeNotesfromLabel(int id, String labelName) {
 	         Note notes = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("Note Not found"));
           Label labelModel = labelRepository.findByLabelName(labelName);
           notes.getLabelModelList().remove(labelModel);
           Note n = new Note(notes, notes.getLabelModelList());
         return noteRepository.save(n);
       }
       public List<Note> getNoteByTitle(String keyword) {
		   return noteRepository.findByTitle(keyword);
		  }	  

}
