package br.ufes.inf.lied.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.inf.lied.objects.LinkData;
import br.ufes.inf.lied.objects.NodeData;
import br.ufes.inf.lied.objects.ObjetoJSON;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

@CrossOrigin
@RestController
public class ISController {

	@RequestMapping(path = "/", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ObjetoJSON MapConceptRequest(@RequestBody ObjetoJSON objeto) throws IOException {
		
		// ------------------------------ LANGUAGE DETECTOR (PT OR EN)------------------------------\\
		LanguageDetectorModel m = new LanguageDetectorModel(Thread.currentThread().getContextClassLoader().getResourceAsStream("langdetect-183.bin"));

		String inputText = "";
		for (NodeData word : objeto.getNodeDataArray()) {
			if (word.getCategory().equals("relation"))
				inputText += word.getText() + " ";
		}

		LanguageDetector myCategorizer = new LanguageDetectorME(m);

		// Get the most probable language
		Language bestlanguage = myCategorizer.predictLanguage(inputText);
		String lang = bestlanguage.getLang();
		// -------------------------------------------------------------------------------------------\\

		if (lang.equals("eng")) {

			for (int i = 0; i < objeto.getNodeDataArray().size(); i++) {

				if (objeto.getNodeDataArray().get(i).getCategory().equals("relation")) {
					NodeData relacao = objeto.getNodeDataArray().get(i);

					// Instantiating SimpleTokenizer class
					SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;

					// Tokenizing the given sentence
					String tokens[] = simpleTokenizer.tokenize(relacao.getText());

					// Loading Parts of speech-maxent model
					InputStream modelIn = Thread.currentThread().getContextClassLoader().getResourceAsStream("en-pos-maxent.bin");
					POSModel model = new POSModel(modelIn);
					POSTaggerME tagger = new POSTaggerME(model);

					// Generating tags
					String[] tags = tagger.tag(tokens);

					// loading the dictionary to input stream
					InputStream dictLemmatizer = Thread.currentThread().getContextClassLoader().getResourceAsStream("en-lemmatizer.bin");
					// loading the lemmatizer with dictionary
					DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

					// finding the lemmas
					String[] lemmas = lemmatizer.lemmatize(tokens, tags);

					for (int k = 0; k < tokens.length; k++) {

						if (tags[k].startsWith("V")) // verifica se o token é verbo
						{
							// ------------------------- IMPLICACAO LOCAL --------------------\\
							if (lemmas[k].equals("be") || lemmas[k].equals("have") || lemmas[k].equals("take")
									|| lemmas[k].equals("can") || lemmas[k].equals("contain") || lemmas[k].equals("compound")
									|| lemmas[k].equals("hold") || lemmas[k].equals("keep") || lemmas[k].equals("own")
									|| lemmas[k].equals("possess")) {
								for (int j = 0; j < objeto.getLinkDataArray().size(); j++) {
									if (objeto.getLinkDataArray().get(j).getFrom().equals(relacao.getKey())
											|| objeto.getLinkDataArray().get(j).getTo().equals(relacao.getKey()))
										objeto.getLinkDataArray().get(j).setColor("Yellow");
								}
							}
							// --------------------------------------------------------------\\

							// ------------------------- IMPLICACAO SISTEMICA --------------------\\
							else {
								Scanner txtscan = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("vtd-en.txt"));
								while (txtscan.hasNextLine()) {
									String str = txtscan.nextLine();
									if (str.indexOf(lemmas[k]) != -1) {
										for (int j = 0; j < objeto.getLinkDataArray().size(); j++) {
											if (objeto.getLinkDataArray().get(j).getFrom().equals(relacao.getKey())
													|| objeto.getLinkDataArray().get(j).getTo().equals(relacao.getKey()))
												objeto.getLinkDataArray().get(j).setColor("Green");
										}
									}
								}
								txtscan.close();
							}
							// --------------------------------------------------------------\\
						}
					}
				}
			}
		}

		if (lang.equals("por")) {

			ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
			Analyzer cogroo = factory.createPipe();

			for (int i = 0; i < objeto.getNodeDataArray().size(); i++) {

				if (objeto.getNodeDataArray().get(i).getCategory().equals("relation")) {
					NodeData relacao = objeto.getNodeDataArray().get(i);

					Document document = new DocumentImpl();
					document.setText(relacao.getText());

					cogroo.analyze(document);

					for (Sentence sentence : document.getSentences()) {

						for (Token token : sentence.getTokens()) {

							String lemmas = Arrays.toString(token.getLemmas());
							String pos = token.getPOSTag();

							if (pos.startsWith("v")) // verifica se o token é verbo
							{
								lemmas = lemmas.replaceAll("\\p{P}", ""); // remove os colchetes das palavras com lemma

								// ------------------------- IMPLICACAO LOCAL --------------------\\
								if (lemmas.equals("ser") || lemmas.equals("ter") || lemmas.equals("conter")
										|| lemmas.equals("possuir") || lemmas.equals("estar") || lemmas.equals("haver")) {
									for (int j = 0; j < objeto.getLinkDataArray().size(); j++) {
										if (objeto.getLinkDataArray().get(j).getFrom().equals(relacao.getKey())
												|| objeto.getLinkDataArray().get(j).getTo().equals(relacao.getKey()))
											objeto.getLinkDataArray().get(j).setColor("Yellow");
									}
								}
								// --------------------------------------------------------------\\
								else {
									// ------------------------- IMPLICACAO SISTEMICA --------------------\\
									Scanner txtscan = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("vtd-pt.txt"));
									while (txtscan.hasNextLine()) {
										String str = txtscan.nextLine();
										if (str.equals(lemmas)) {
											for (int j = 0; j < objeto.getLinkDataArray().size(); j++) {
												if (objeto.getLinkDataArray().get(j).getFrom().equals(relacao.getKey())
														|| objeto.getLinkDataArray().get(j).getTo().equals(relacao.getKey()))
													objeto.getLinkDataArray().get(j).setColor("Green");
											}
										}
									}
									txtscan.close();
									// --------------------------------------------------------------\\
								}
							}
						}
					}
				}
			}
		}
		
		// ------------------------- IMPLICACAO ESTRUTURAL --------------------\\
		UUID key = null;
		for (int k = 0; k < objeto.getNodeDataArray().size(); k++) {
			
			NodeData relation = objeto.getNodeDataArray().get(k);
			if (relation.getCategory().equals("relation"))
				key = relation.getKey();
			
			for (int j = 0; j < objeto.getLinkDataArray().size(); j++) {
				for (int l = 0; l < objeto.getLinkDataArray().size(); l++) {
					
					LinkData aux = objeto.getLinkDataArray().get(l);
					LinkData aux2 = objeto.getLinkDataArray().get(j);
					
					if (aux.getColor() != "Yellow" && aux2.getColor() != "Yellow")
						if (!aux2.getFrom().equals(aux.getFrom()) && aux2.getTo().equals(key) && aux.getTo().equals(key)) {
							objeto.getLinkDataArray().get(l).setColor("Blue");
							objeto.getLinkDataArray().get(j).setColor("Blue");
							
							for (int z = 0; z < objeto.getLinkDataArray().size(); z++) {
								LinkData aux3 = objeto.getLinkDataArray().get(z);
								if (aux3.getFrom().equals(key))
									objeto.getLinkDataArray().get(z).setColor("Blue");
							}
						}
					}
				}
			}
		// --------------------------------------------------------------------\\
 		
		// para relações que não forem identificadas em nenhuma implicação significante,
		// é definida a cor preta
		for (LinkData i : objeto.getLinkDataArray()) {
			if (i.getColor() == null)
				i.setColor("Black");
		}
		return objeto;
	}
}
