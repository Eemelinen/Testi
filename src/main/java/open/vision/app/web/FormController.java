package open.vision.app.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import open.vision.app.domain.Answer;
import open.vision.app.domain.AnswerCreatorObject;
import open.vision.app.domain.AnswerOption;
import open.vision.app.domain.AnswerOptionRepository;
import open.vision.app.domain.Question;
import open.vision.app.domain.QuestionRepository;


@Controller
public class FormController {

	@Autowired
	private QuestionRepository qRepo;
	
	@Autowired 
	private AnswerOptionRepository aRepo;
	
	// == Lomakesivulla tässä mallissa yksi kovakoodattu kysymys ja sen vastausvaihtoehdot. Miten useilla kysymyksillä? ==
	@GetMapping("/answerForm")
	public String answerForm(Model model) {
		
		model.addAttribute("question", qRepo.findByType("Lukkarikone5").get(0));
		
		List<AnswerOption> answerOptions =  qRepo.findByType("Lukkarikone5").get(0).getAnswerOptions();
		
		model.addAttribute("answerOptions", answerOptions);
		
		return "answerForm";
	}
	
//	@RequestMapping(value="/submit", method=RequestMethod.POST)
//	public @ResponseBody Question sendAnswerRest(@PathVariable("questionId") Long id) {
//		
//		qRepo.save();
//		return answer;
//	}
	
	// == Kysymys otetaan vastaan ja tallennetaan tietokantaan. ==
	@PostMapping("/answerForm")
	public String answerSubmit(@ModelAttribute Question question) {
		
		qRepo.save(question);
		
		return "result";
	}
	
	//Question list
	@RequestMapping(value="/list")
	public String getFormList(Model model) {
		List<Question> questions = (List<Question>) qRepo.findAll();
		model.addAttribute("questions", questions);
		return "notaform";
	}
	
	//Question form
	@RequestMapping(value="/form")
	public String getForm(Model model) {
		model.addAttribute("questions", qRepo.findAll());
		model.addAttribute("answer", aRepo.findAll());
		return "singleform";
	}
	
	//Submit form
	@RequestMapping(value="/submit", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute AnswerOption answer) {
		aRepo.save(answer);
		return "redirect:/singleform";
	}
	
	//RESTful service to get all questions
	@RequestMapping(value="/questions", method = RequestMethod.GET)
	public @ResponseBody List<Question> getQuestionRest(){
		return (List<Question>) qRepo.findAll();
	}
	
	//RESTful service to get one questions
	@RequestMapping(value="/question/{id}", method=RequestMethod.GET)
	public @ResponseBody Optional<Question> findQuestionRest(@PathVariable("id") Long id) {
		return qRepo.findById(id);
	}
	
	// == RESTful service to get one question by type ==
	@RequestMapping(value="/questionType/{type}", method=RequestMethod.GET)
	public @ResponseBody Question findQuestionsRest(@PathVariable("type") String type) {
		return qRepo.findByType(type).get(0);
	}
	
	//RESTful service to get all answer options
	@RequestMapping(value="/answerOptions", method = RequestMethod.GET)
	public @ResponseBody List<AnswerOption> getAnswerRest(){
		return (List<AnswerOption>) aRepo.findAll();
	}
	
	//RESTful service to get one answer options by id
	@RequestMapping(value="/answerOption/{answerid}", method=RequestMethod.GET)
	public @ResponseBody Optional<AnswerOption> findAnswerRest(@PathVariable("answerid") Long id) {
		return aRepo.findById(id);
	}
}
