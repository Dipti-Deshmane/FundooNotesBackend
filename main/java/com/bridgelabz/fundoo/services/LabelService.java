package com.bridgelabz.fundoo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.exception.NoteNotFoundException;
import com.bridgelabz.fundoo.exception.UserAlreadyExistsException;
import com.bridgelabz.fundoo.model.Label;
import com.bridgelabz.fundoo.model.Note;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.repository.LabelRepository;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.repository.UserRepository;

@Service
public class LabelService {
	@Autowired
	private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;
	    
    @Autowired
    private NoteRepository noteRepository;
	    
    public Label createLabel(String labelName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    Label existingLabel = labelRepository.findByLabelName(labelName);
        
	    if (existingLabel != null) {
            throw new UserAlreadyExistsException("Label already exists");
        }
	    Label labelModel = new Label(labelName, user);
            return labelRepository.save(labelModel);
        }
	    
	    public List<Label> getAllLabelNotes() {
	        return labelRepository.findAll();
	       
	    }
	
	public List<Label> getAllLabelsByUserId(int userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userRepository.findByEmail(userEmail).orElseThrow();
		           return labelRepository.findAllLabelsByUserId(userId);
	}
	    
	public void deleteLabelById(int labelId) {
	    labelRepository.findById(labelId).orElseThrow(() -> new NoteNotFoundException("Label with id " + labelId + " not found"));
	    labelRepository.deleteById(labelId);
	}

	
	public Label editLabelById(int labelId, String labelName) {
		Label labelModel = labelRepository.findById(labelId).orElseThrow(() -> new NoteNotFoundException("Label not found"));
        labelModel.setLabelName(labelName);
        return labelRepository.save(labelModel);
    }

	 
    public List<Note> getAllLabelNotesByLabelId(int labelId) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		User user = userRepository.findByEmail(userEmail).orElseThrow();
		int userId = user.getUserId();
	          
		List<Note> notesModelList = new ArrayList<>();
	    Label labelModel = labelRepository.findLabelByUserId(userId, labelId);
	    if (labelModel != null) {
	            List<Integer> notesIdList = labelRepository.findAllNotesIdByLabelId(labelId);
	            System.out.println(notesIdList);
           
	    for (int i = 0; i < notesIdList.size(); i++) {
	        noteRepository.findById(notesIdList.get(i)).ifPresent(notesModelList::add);
	     }
	          System.out.println(notesModelList);
	          return notesModelList;
	          }  else {
	            throw new UserAlreadyExistsException("Label not found");
	        }
	    }


	    public String getCurrentLabel(int labelId) {
	        Label labelModel = labelRepository.findById(labelId).orElseThrow(() -> new NoteNotFoundException("Label Data Not Found"));
	        if (labelModel != null) {
	            return labelModel.getLabelName();
	        } else {
	            return null;
	        }
	    }

}
