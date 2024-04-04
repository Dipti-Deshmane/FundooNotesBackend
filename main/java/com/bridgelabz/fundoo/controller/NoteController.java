package com.bridgelabz.fundoo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.DTO.ResponseDTO;
import com.bridgelabz.fundoo.model.Note;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.services.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/user/createNote")
    public ResponseEntity<Object> createNote(@RequestBody Note noteRequest) {
       Note note = new Note();
       note.setTitle(noteRequest.getTitle());
       note.setDescription(noteRequest.getDescription());
       note.setPinned(noteRequest.isPinned());
       note.setArchive(note.isArchive());	
       
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userEmail = authentication.getName();
       
       User user = userRepository.findByEmail(userEmail).orElseThrow();
       note.setUser(user);
       
       return ResponseEntity.ok(noteRepository.save(note));
    }
    
    
    
    @GetMapping("/user/get")
    public ResponseEntity<?> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
       
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Note> userNotes = user.getNotes();
       
        if (!userNotes.isEmpty()) {
            return new ResponseEntity<>(userNotes, HttpStatus.OK);
        } else {
        	Response response = new Response();
            response.setCode(400);
            response.setMessage("No notes present in the list.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/getnotebyid/{id}")
    public Note getNoteById(@PathVariable int id) {
        return noteService.getNoteById(id);
    }
    
    @PutMapping("/updatenote/{id}")
    public ResponseEntity<String> updateNote(@PathVariable int id, @RequestBody Note note) {
    	noteService.updateNote(id,note);
		return ResponseEntity.ok("Note updated successfully.");
    }
    
    @DeleteMapping("/delete")
	public String deleteAllNotes(){	
    	noteService.deleteAllNotes();
		return "All Notes Deleted successfully.";
	}
    
    @DeleteMapping("/delete/{id}")
    public String deleteNote(@PathVariable int id) {
    	noteService.deleteNoteById(id);
		return "Note with id " + id +" deleted Successfully.";
    }
    @PutMapping("/setarchive/{id}")
    public String setNotesToArchive(@PathVariable("id") int id){
        noteService.setNoteToArchive(id);
        return "Note Archived";
    }
    
    @PutMapping("/setPinned/{id}")
    public String setNoteToPinned(@PathVariable("id") int id){
        noteService.setNoteToPinned(id);
        return "Note Pinned";
    }
    @PutMapping("/setunarchive/{id}")
    public String setNoteToUnArchive(@PathVariable("id")int id){
    	noteService.setNoteToUnArchive(id);
        return "Note Unarchived";
    }
    
    @PutMapping("/setunpinned/{id}")
    public String setNoteToUnPinned(@PathVariable("id")int id){
    	noteService.setNoteToUnPinned(id);
        return "Note Unpinned";
    }
    
    @PutMapping("/setuntrash/{id}")
    public String setNoteToUnTrash(@PathVariable("id")int id){
    	noteService.setNoteToUnTrash(id);
        return "Note Untrashed";
    }
    
    @PutMapping("/settrash/{id}")
    public String setNoteToTrash(@PathVariable("id")int id){
    	noteService.setNoteToTrash(id);
        return "Note Trashed";
    }
    
    
    
    @GetMapping("/getarchived")
    public ResponseEntity<List<Note>> getAllArchivedNotes() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   	    String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        int userId = user.getUserId();
        
        List<Note> archivedNotes = noteRepository.findAllArchiveNotesByUserId(userId);
        if (!archivedNotes.isEmpty()) {
            return new ResponseEntity<>(archivedNotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/getpinned")
    public ResponseEntity<List<Note>> getAllPinnedNotes() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   	    String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        int userId = user.getUserId();
        
        List<Note> pinnedNotes = noteRepository.findAllPinnedNotesByUserId(userId);
        if (!pinnedNotes.isEmpty()) {
            return new ResponseEntity<>(pinnedNotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    @GetMapping("/gettrashed")
    public ResponseEntity<List<Note>> getAllTrashedNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        int userId = user.getUserId();
        
        List<Note> trashNotes = noteRepository.findAllTrashNotesByUserId(userId);
        if (!trashNotes.isEmpty()) {
            return new ResponseEntity<>(trashNotes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    @PutMapping("/setlabel/{id}/{labelName}")

    public ResponseEntity<ResponseDTO> setNotesToLabel(@PathVariable("id") int id, @PathVariable String labelName) {

        Note note = noteService.addNotesToLabel(id, labelName);

        ResponseDTO responseDTO = new ResponseDTO("Label Attached", note);

        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);

    }

    @PutMapping("/removelabel/{id}/{labelName}")
    public ResponseEntity<ResponseDTO> deleteNotesfromLabel(@PathVariable("id") int id, @PathVariable("labelName") String labelName) {
        Note note = noteService.removeNotesfromLabel(id, labelName);
        ResponseDTO responseDTO = new ResponseDTO("Label Edited Succesfully", note);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/getlabelname/{id}")
    public  ResponseEntity<ResponseDTO> getLabelNamesByNoteId(@PathVariable("id") int id){
        List<String> labelNameList = noteService.getLabelNamesByNoteId(id);
        ResponseDTO responseDTO = new ResponseDTO("Get call successful", labelNameList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
    
    @GetMapping("/getnotebytitle/{keyword}")
    public ResponseEntity<List<Note>> getNoteByTitle(@PathVariable String keyword) {
        List<Note> notes = noteService.getNoteByTitle(keyword);
        if (!notes.isEmpty()) {
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
}
