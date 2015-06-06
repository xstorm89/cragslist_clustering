// Copyright 2015 smanna@cpp.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.


import java.util.HashSet;

public class StopWords 
{
	private HashSet<String> words;
	
	public StopWords()
	{
		words = new HashSet<>();
		words.add("a");
		words.add("about");
		words.add("above");
		words.add("after");
		words.add("again");
		words.add("against");
		words.add("all");
		words.add("am");
		words.add("an");
		words.add("and");
		words.add("any");
		words.add("are");
		words.add("arent");
		words.add("as");
		words.add("at");
		words.add("be");
		words.add("because");
		words.add("been");
		words.add("before");
		words.add("being");
		words.add("below");
		words.add("between");
		words.add("both");
		words.add("but");
		words.add("by");
		words.add("cant");
		words.add("cannot");
		words.add("could");
		words.add("couldnt");
		words.add("did");
		words.add("didnt");
		//words.add("do");
		words.add("does");
		words.add("doesnt");
		words.add("doing");
		words.add("dont");
		words.add("down");
		words.add("during");
		words.add("each");
		words.add("few");
		words.add("for");
		words.add("from");
		words.add("further");
		words.add("had");
		words.add("hadnt");
		words.add("has");
		words.add("hasnt");
		words.add("have");
		words.add("havent");
		words.add("having");
		words.add("he");
		words.add("hed");
		words.add("hes");
		words.add("her");
		words.add("here");
		words.add("heres");
		words.add("hers");
		words.add("herself");
		words.add("him");
		words.add("himself");
		words.add("his");
		words.add("how");
		words.add("hows");
		words.add("i");
		words.add("id");
		words.add("im");
		words.add("ive");
		//words.add("if");
		words.add("in");
		words.add("into");
		words.add("is");
		words.add("isnt");
		words.add("it");
		words.add("its");
		words.add("itself");
		words.add("lets");
		words.add("me");
		words.add("more");
		words.add("most");
		words.add("mustnt");
		words.add("my");
		words.add("myself");
		words.add("no");
		words.add("nor");
		words.add("not");
		words.add("of");
		words.add("off");
		words.add("on");
		words.add("once");
		words.add("only");
		words.add("or");
		words.add("other");
		words.add("ought");
		words.add("our");
		words.add("ours	ourselves");
		words.add("out");
		words.add("over");
		words.add("own");
		words.add("same");
		words.add("shant");
		words.add("she");
		words.add("shed");
		words.add("shes");
		words.add("should");
		words.add("shouldnt");
		words.add("so");
		words.add("some");
		words.add("such");
		words.add("than");
		words.add("that");
		words.add("thats");
		words.add("the");
		words.add("their");
		words.add("theirs");
		words.add("them");
		words.add("themselves");
		words.add("then");
		words.add("there");
		words.add("theres");
		words.add("these");
		words.add("they");
		words.add("theyd");
		words.add("theyll");
		words.add("theyre");
		words.add("theyve");
		//words.add("this");
		words.add("those");
		words.add("through");
		words.add("to");
		words.add("too");
		words.add("under");
		words.add("until");
		words.add("up");
		words.add("very");
		words.add("was");
		words.add("wasnt");
		words.add("we");
		words.add("wed");
		words.add("well");
		words.add("were");
		words.add("weve");
		words.add("were");
		words.add("werent");
		//words.add("what");
		words.add("whats");
		words.add("when");
		words.add("whens");
		words.add("where");
		words.add("wheres");
		words.add("which");
		words.add("while");
		words.add("who");
		words.add("whos");
		words.add("whom");
		words.add("why");
		words.add("whys");
		words.add("with");
		words.add("wont");
		words.add("would");
		words.add("wouldnt");
		//words.add("you");
		words.add("youd");
		words.add("youll");
		words.add("youre");
		words.add("youve");
		words.add("your");
		words.add("yours");
		words.add("yourself");
		words.add("yourselves");
	}
	
	public boolean isStopWord(String a)
	{
		return words.contains(a);
	}
}
