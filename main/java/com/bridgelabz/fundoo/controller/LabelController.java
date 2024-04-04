package com.bridgelabz.fundoo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.model.Label;
import com.bridgelabz.fundoo.model.Note;
import com.bridgelabz.fundoo.response.ResponseDTO;
import com.bridgelabz.fundoo.services.LabelService;

@RestController
@RequestMapping("/label")
public class LabelController {
	
	@Autowired
	private LabelService labelService;

	    @PostMapping("/createlabel/{labelName}")
	    public ResponseEntity<ResponseDTO> createLabel(@PathVariable("labelName") String labelName){
	        Label labelModel = labelService.createLabel(labelName);
	        ResponseDTO responseDTO = new ResponseDTO("Label Created Successfully", labelModel);
	        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
	    }

	    @GetMapping("/getlabelnotes")
	    public List<Label> getAllLabelledNotes(){
	        return labelService.getAllLabelNotes();
	     
	    }

  	    @GetMapping("/getlabelsbyuser/{userId}")
 	    public ResponseEntity<ResponseDTO> getAllLabelsByUserId(@PathVariable int userId ){
	        List<Label> labelModelList = labelService.getAllLabelsByUserId(userId);
	        ResponseDTO responseDTO = new ResponseDTO("Successfully label retrieved by user", labelModelList);
	        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
	    }
  	    
	    @GetMapping("/getalllabelnotesbylabelid/{labelId}")
	    public ResponseEntity<ResponseDTO> getAllLabelNotesByLabelId(@PathVariable("labelId") int labelId){
	        List<Note> notesModelList = labelService.getAllLabelNotesByLabelId(labelId);
	        ResponseDTO responseDTO = new ResponseDTO("Successfully labelled notes retrieved", notesModelList);
	        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
	    }

	    @PutMapping("/editlabelbyid/{labelId}/{labelName}")
	    public ResponseEntity<ResponseDTO> editLabelById(@PathVariable("labelId") int labelId, @PathVariable("labelName") String labelName){
	        Label label = labelService.editLabelById(labelId, labelName);
	        ResponseDTO responseDTO = new ResponseDTO("Label edited Successfully", label);
	        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	    }

	    @GetMapping("/getcurrentlabel/{labelId}")
	    public  ResponseEntity<ResponseDTO> getCurrentLabel(@PathVariable("labelId") int labelId) {
	        String labelName = labelService.getCurrentLabel(labelId);
	        ResponseDTO responseDTO = new ResponseDTO("Current label retrieved", labelName);
	        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
	    }
	    
	    @DeleteMapping("/deletelabelbyid/{labelId}")
	    public ResponseEntity<ResponseDTO> deleteLabelById(@PathVariable("labelId") int labelId){
	        labelService.deleteLabelById(labelId);
	        ResponseDTO responseDTO = new ResponseDTO();
	        responseDTO.setMessage("Label with Id " + labelId + " deleted Successfully");
	        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	    }
}
