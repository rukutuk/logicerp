package pohaci.gumunda.titis.application;


import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * SimpleCalculator: parses a string input and evaluates it.
 * Each token must be separated with space, return, or tab
 * @author dewax4
 * @version 1.0
 */
public class SimpleCalculator {
  protected CalculatorNode m_root = null;
  protected String LASTOPERATION = "";
  /**
   * parse the input string. PROBLEM: StringTokenizer is not a real tokenizer, it is just
   * a normal string splitter. So this will only works if a space (or tab, or linefeed) delimits each token from another
   * @param input The string to be parsed, each token delimited by space (or another StringTokenizer default delimiter)
   * @throws Exception something goes wrong
   */
  protected void parseInput(String input)  throws Exception{
    StringTokenizer tokenizer = new StringTokenizer(input);
    int tokencount = tokenizer.countTokens();
    if(tokencount == 1){
      String number = tokenizer.nextToken();
      if(!isNumber(number)){
        throw new Exception("Invalid input: " + number);
      }

      m_root = new CalculatorNode(number);
    }else{
    	try{
    		if(!tokenizer.hasMoreTokens())
    			return;
    		String token = tokenizer.nextToken();
    		if(isNumber(token)){
    			CalculatorNode node = new CalculatorNode(token);

    			LASTOPERATION = CalculatorNode.NUMBER;
    			m_root = parseInput(node, tokenizer);
    		}else if(token.equals(CalculatorNode.OPENBRACKET)){
    			//LASTOPERATION = CalculatorNode.OPENBRACKET;
    			m_root = parseTokensInBracket(null, tokenizer);
    		}else{
    			throw new Exception("Invalid input: " + token);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
  }

/**
 * Builds calculation tree from input string
 * @param constructed previously constructed node
 * @param tokenizer tokenizer built upon input string
 * @return CalculatorNode root of newly created tree
 * @throws Exception
 */
  protected CalculatorNode parseInput(CalculatorNode constructed, StringTokenizer tokenizer)  throws Exception{
    int tokencount = tokenizer.countTokens();
    if(tokencount >= 2){
      String ftoken = tokenizer.nextToken();//token pertama
      String stoken = tokenizer.nextToken();//token kedua
      if(isNumber(stoken)){
        if(ftoken.equals(CalculatorNode.SUBTRACTION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.SUBTRACTION);
          CalculatorNode leaf = new CalculatorNode(stoken);
          node.setLeftNode(constructed);
          node.setRightNode(leaf);

          LASTOPERATION = CalculatorNode.SUBTRACTION;
          return parseInput(node, tokenizer);
        }if(ftoken.equals(CalculatorNode.ADDITION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.ADDITION);
          CalculatorNode leaf = new CalculatorNode(stoken);
          node.setLeftNode(constructed);
          node.setRightNode(leaf);

          LASTOPERATION = CalculatorNode.ADDITION;
          return parseInput(node, tokenizer);
        }if(ftoken.equals(CalculatorNode.MULTIPLICATION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.MULTIPLICATION);
          CalculatorNode leaf = new CalculatorNode(stoken);
          node.setRightNode(leaf);

          if(LASTOPERATION.equals(CalculatorNode.NUMBER)){
            node.setLeftNode(constructed);

            LASTOPERATION = CalculatorNode.MULTIPLICATION;
            return parseInput(node, tokenizer);
            }else if(LASTOPERATION.equals(CalculatorNode.ADDITION) ||
                     LASTOPERATION.equals(CalculatorNode.SUBTRACTION)){
              node.setLeftNode(constructed.getRightNode());
              constructed.setRightNode(node);

              LASTOPERATION = CalculatorNode.MULTIPLICATION;
              return parseInput(constructed, tokenizer);
              }else if(LASTOPERATION.equals(CalculatorNode.MULTIPLICATION) ||
                       LASTOPERATION.equals(CalculatorNode.DIVISION)){
                node.setLeftNode(constructed);

                LASTOPERATION = CalculatorNode.MULTIPLICATION;
                return parseInput(node, tokenizer);
              }else
                return null;
        }else if(ftoken.equals(CalculatorNode.DIVISION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.DIVISION);
          CalculatorNode leaf = new CalculatorNode(stoken);

          if(Double.valueOf(stoken).doubleValue() == 0.0)
            throw new Exception("Pembagian dengan 0 dilarang");

          node.setRightNode(leaf);

          if(LASTOPERATION.equals(CalculatorNode.NUMBER)){
            node.setLeftNode(constructed);

            LASTOPERATION = CalculatorNode.DIVISION;
            return parseInput(node, tokenizer);
            }else if(LASTOPERATION.equals(CalculatorNode.ADDITION) ||
                     LASTOPERATION.equals(CalculatorNode.SUBTRACTION)){
              node.setLeftNode(constructed.getRightNode());
              constructed.setRightNode(node);

              LASTOPERATION = CalculatorNode.DIVISION;
              return parseInput(constructed, tokenizer);
              }else if(LASTOPERATION.equals(CalculatorNode.MULTIPLICATION) ||
                       LASTOPERATION.equals(CalculatorNode.DIVISION)){
                node.setLeftNode(constructed);

                LASTOPERATION = CalculatorNode.DIVISION;
                return parseInput(node, tokenizer);
              }else
                return null;
        }else
          return null;
      }else if(stoken.equals(CalculatorNode.OPENBRACKET)){
        String lastoperation = LASTOPERATION;
        CalculatorNode partialnode = parseTokensInBracket(null, tokenizer);
        LASTOPERATION = lastoperation;

        if(ftoken.equals(CalculatorNode.SUBTRACTION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.SUBTRACTION);
          node.setLeftNode(constructed);
          node.setRightNode(partialnode);

          LASTOPERATION = CalculatorNode.SUBTRACTION;
          return parseInput(node, tokenizer);
        }if(ftoken.equals(CalculatorNode.ADDITION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.ADDITION);
          node.setLeftNode(constructed);
          node.setRightNode(partialnode);

          LASTOPERATION = CalculatorNode.ADDITION;
          return parseInput(node, tokenizer);
        }if(ftoken.equals(CalculatorNode.MULTIPLICATION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.MULTIPLICATION);
          node.setRightNode(partialnode);

          if(LASTOPERATION.equals(CalculatorNode.NUMBER)){
            node.setLeftNode(constructed);

            LASTOPERATION = CalculatorNode.MULTIPLICATION;
            return parseInput(node, tokenizer);
            }else if(LASTOPERATION.equals(CalculatorNode.ADDITION) ||
                     LASTOPERATION.equals(CalculatorNode.SUBTRACTION)){
              node.setLeftNode(constructed.getRightNode());
              constructed.setRightNode(node);

              LASTOPERATION = CalculatorNode.MULTIPLICATION;
              return parseInput(constructed, tokenizer);
              }else if(LASTOPERATION.equals(CalculatorNode.MULTIPLICATION) ||
                       LASTOPERATION.equals(CalculatorNode.DIVISION)){
                node.setLeftNode(constructed);

                LASTOPERATION = CalculatorNode.MULTIPLICATION;
                return parseInput(node, tokenizer);
              }else
                return null;
        }else if(ftoken.equals(CalculatorNode.DIVISION)){
          CalculatorNode node = new CalculatorNode(CalculatorNode.DIVISION);
          String partialvalue = partialnode.getNodeValue();

          if(Double.valueOf(partialvalue).doubleValue() == 0.0)
            throw new Exception("Pembagian dengan 0 dilarang");

          node.setRightNode(partialnode);

          if(LASTOPERATION.equals(CalculatorNode.NUMBER)){
            node.setLeftNode(constructed);

            LASTOPERATION = CalculatorNode.DIVISION;
            return parseInput(node, tokenizer);
            }else if(LASTOPERATION.equals(CalculatorNode.ADDITION) ||
                     LASTOPERATION.equals(CalculatorNode.SUBTRACTION)){
              node.setLeftNode(constructed.getRightNode());
              constructed.setRightNode(node);

              LASTOPERATION = CalculatorNode.DIVISION;
              return parseInput(constructed, tokenizer);
              }else if(LASTOPERATION.equals(CalculatorNode.MULTIPLICATION) ||
                       LASTOPERATION.equals(CalculatorNode.DIVISION)){
                node.setLeftNode(constructed);

                LASTOPERATION = CalculatorNode.DIVISION;
                return parseInput(node, tokenizer);
              }else
                return null;
        }else
          return null;
      }else{
        throw new Exception("Invalid Input: " + ftoken + " " + stoken);
      }
    }else if(tokencount == 0){
      return constructed;
    }else
      return constructed;
  }


  protected CalculatorNode parsePartialInput(String input)  throws Exception{
    StringTokenizer tokenizer = new StringTokenizer(input);
    int tokencount = tokenizer.countTokens();
    if(tokencount == 1){
      String number = tokenizer.nextToken();
      if(!isNumber(number)){
        throw new Exception("Invalid input: " + number);
      }

      return new CalculatorNode(number);
    }else{
      String token = tokenizer.nextToken();
      if(isNumber(token)){
        CalculatorNode node = new CalculatorNode(token);

        LASTOPERATION = CalculatorNode.NUMBER;
        return parseInput(node, tokenizer);
      }else if(token.equals(CalculatorNode.OPENBRACKET)){
        //LASTOPERATION = CalculatorNode.OPENBRACKET;
        return parseTokensInBracket(null, tokenizer);
      }else{
        throw new Exception("Invalid input: " + token);
      }
    }
  }


  protected CalculatorNode parseTokensInBracket(CalculatorNode constructed, StringTokenizer tokenizer)  throws Exception{
    String inbracket = "";
    boolean closebracketfound = false;

    while(tokenizer.hasMoreElements()){
      String token = tokenizer.nextToken();
      if(token.equals(CalculatorNode.CLOSEBRACKET)){
        CalculatorNode partialnode = parsePartialInput(inbracket);
        double value = execute(partialnode);
        //CalculatorNode node = new CalculatorNode(String.valueOf(value));
        CalculatorNode node = new CalculatorNode(new BigDecimal(value).toString());
        if(constructed == null){
          constructed = node;
          LASTOPERATION = CalculatorNode.NUMBER;
        }else {
          constructed.setRightNode(node);
        }
        closebracketfound = true;
        break;
      }else if(token.equals(CalculatorNode.OPENBRACKET)){
        constructed = parseTokensInBracket(constructed, tokenizer);
      }else{
        inbracket += token + " ";
      }
    }

    if(!closebracketfound){
      throw new Exception("Kurung buka tidak menemukan kurung tutup");
    }

    return parseInput(constructed, tokenizer);
  }

  protected boolean isNumber(String token){
    char ctoken[] = token.toCharArray();
    for(int i=0; i < ctoken.length; i++){
      if(!Character.isDigit(ctoken[i]) && ((ctoken[i] != '.') && (ctoken[i] != 'E')))
        return false;
    }

    return true;
  }

  protected void print(CalculatorNode node){
    if(node == null)
      return;
    if(node.m_left != null)
      print(node.m_left);
    System.out.print(node.m_node);
    if(node.m_right != null)
      print(node.m_right);
  }

  /**
   * calculates the result of given input string during construction
   * @return value of the result as double
   * @throws Exception
   */
  public double execute() throws Exception{
    if(m_root == null)
      return 0.0;

    return execute(m_root);
  }

  private double execute(CalculatorNode node) throws Exception{
    if(isNumber(node.m_node))
      return Double.valueOf(node.m_node).doubleValue();

    double leftsum = execute(node.m_left);
    double rightsum = execute(node.m_right);
    if(node.m_node.equals(CalculatorNode.ADDITION))
      return leftsum + rightsum;
    else if(node.m_node.equals(CalculatorNode.SUBTRACTION))
      return leftsum - rightsum;
    else if(node.m_node.equals(CalculatorNode.MULTIPLICATION))
      return leftsum * rightsum;
    else  if(node.m_node.equals(CalculatorNode.DIVISION)){
      if(rightsum == 0.0)
        throw new Exception("Fatal Error: Pembagian dengan 0");
      return leftsum/rightsum;
    }else{
      throw new Exception("Fatal Error: Kesalahan tidak diketahui");
    }
  }

  /**
   * Creates a simple calculator to calculate an input String.
   * Tokens (operand and operators) must be separated with space, or tab, or return.
   * @param input a sequence of number and operators, separated with space/tab/return
   * @throws Exception if something went wrong
   */
  public SimpleCalculator(String input) throws Exception{
    parseInput(input);
    System.out.print("Input: ");
    print(m_root);
    System.out.print(" = " + execute());
  }

 /* public static void main(String[] args){
    String input = "7 + ( 2 + 3 ) * ( 3 + 1 ) / ( 6 - 2 )";
    new SimpleCalculator(input);
  }*/
}