// Susanna Xiangyi Liu
// CSE 143 AV with Ian S Wohlers
// Homework 8
// HuffmanCode has the ability to compress data using David A. Huffman's Huffman Code algorithm
// and represent it as a "huffman code". Also has the ability to decompress the
// compressed file to retrieve original data. Allows for more efficient storage space.
import java.io.*;
import java.util.*;

public class HuffmanCode {
   private HuffmanNode overallRoot;

   // Initializes a new HuffmanCode object tree which is used to compress data.
   //
   // Parameter: Takes an array of frequencies where frequencies[i] represents the count of
   // the character with ASCII value i.
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> queue = new PriorityQueue<>();
      for (int i = 0; i < frequencies.length; i++) {
         if (frequencies[i] != 0) {
            HuffmanNode node = new HuffmanNode((char)i, frequencies[i]);
            queue.add(node);
         }
      }
      while (queue.size() > 1) {
         overallRoot = new HuffmanNode(' ', queue.remove(), queue.remove());
         queue.add(overallRoot);
      }
   }

   // Initializes a new HuffmanCode object that is used to decompress the data.
   // Object is recreated into the original tree format in which it was created, through
   // the compression constructor. Object created is a new tree.
   //
   // Parameter: Takes a Scanner that reads a data file. Data file consists of the ascii value
   // of a character as well as its Huffman Code.
   public HuffmanCode(Scanner input) {
      while (input.hasNextLine()) {
         int asciiValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = createTree(overallRoot, code, asciiValue);
      }
   }

   // Parameter: Takes a reference to the current node, a Huffman Code that helps keep track of
   // which root to point to, as well as character's ascii value.
   //
   // post: Reads in previously constructed code. Returns and replaces the current tree with a
   // new one using that information.
   private HuffmanNode createTree(HuffmanNode root, String code, int asciiValue) {
      if (code.length() == 0) {
         root = new HuffmanNode((char)asciiValue, 0);
      } else {
         if (root == null) {
            root = new HuffmanNode(' ', 0);
         }
         if (code.charAt(0) == '0') {
            root.left = createTree(root.left, code.substring(1), asciiValue);
         } else {
            root.right = createTree(root.right, code.substring(1), asciiValue);
         }
      }
      return root;
   }

   // Parameter: Takes a print stream to output to file.
   //
   // post: Stores the current HuffmanNode tree to an output file in standard format.
   public void save(PrintStream output) {
      write(overallRoot, output, "");
   }

   // Parameter: Takes a reference to the current node and a print stream to output to file.
   // Takes a Huffman code that helps keep track of the reference to current node.
   //
   // post: Stores the current HuffmanCode object, represented by a tree, into an output file in
   // standard format.
   private void write(HuffmanNode root, PrintStream output, String code) {
      if (root.left == null && root.right == null) {
         output.println((int)root.data);
         output.println(code);
      } else {
         write(root.left, output, code += 0);
         code = code.substring(0, code.length() - 1);
         write(root.right, output, code += 1);
      }
   }

   // Parameter: Takes in individual bits from an input and a print stream to output to file.
   //
   // post: Stores the corresponding characters and resulting data into an output file. This should
   // serve as the decompressing of a file.
   public void translate(BitInputStream input, PrintStream output) {
      while (input.hasNextBit()) {
         translate(input, output, overallRoot);
      }
   }

   // Parameter: Takes in bits from an input, a print stream to output to file, and a
   // reference to the current node of the HuffmanCode tree.
   //
   // post: Decompresses the tree data into text file data and outputs it into a file.
   private void translate(BitInputStream input, PrintStream output, HuffmanNode root) {
      if (root.left == null && root.right == null) {
         output.write(root.data);
      } else if (input.nextBit() == 0) {
         translate(input, output, root.left);
      } else {
         translate(input, output, root.right);
      }
   }

   // The HuffmanNode class represents a single Huffman Node in the tree.
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public char data; // character stored in this node
      public int frequency; // the number of times the character appears in the data file
      public HuffmanNode left; // reference to left subtree
      public HuffmanNode right; // reference to right subtree

      // Takes a character as well as its frequency. Constructs a single leaf node.
      public HuffmanNode(char data, int frequency) {
         this(data, frequency, null, null);
      }

      // Takes a character data, a reference to the left and right subtrees to construct
      // a new leaf or branch node.
      public HuffmanNode(char data, HuffmanNode left, HuffmanNode right) {
         this.data = data;
         this.frequency = left.frequency + right.frequency;
         this.left = left;
         this.right = right;
      }

      // Takes a character data, the frequency of the character, a reference to the left
      // and right subtrees to construct a new leaf or branch node.
      public HuffmanNode(char data, int frequency, HuffmanNode left, HuffmanNode right) {
         this.data = data;
         this.frequency = frequency;
         this.left = left;
         this.right = right;
      }

      // Takes a different HuffmanNode.
      //
      // Returns a number > 0 if this HuffmanNode's frequency is larger than
      // other frequency.
      // Returns 0 if they have the same frequency.
      // Returns number < 0 if this HuffmanNode's frequency is less than other frequency.
      public int compareTo(HuffmanNode other) {
         return this.frequency - other.frequency;
      }
   }
}
