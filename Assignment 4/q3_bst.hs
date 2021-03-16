import Data.Char
import Data.List.Split
-- Run command "cabal update" then "cabal install split" to install split package


--Data structure for BST
data BST a = Nil | Node (BST a) a (BST a) deriving Show

-- Insertion In BST
insert :: Ord a => BST a -> a -> BST a
insert Nil v = Node Nil v Nil
insert(Node treeLeft root treeRight) v
    | root == v = Node treeLeft root treeRight
    | root < v  = Node treeLeft root (insert treeRight v)
    | root > v  = Node (insert treeLeft v) root treeRight


-- Building a tree from a list, iterative approach
create_tree :: Ord a => [a] -> BST a
create_tree [] = Nil
create_tree (a_head : a_tail) = auxiliary_function (Node Nil a_head Nil) a_tail
    where
        auxiliary_function tree [] = tree
        auxiliary_function tree (a_head : a_tail) = auxiliary_function (insert tree a_head) a_tail


-- Inorder Traversal of the tree
inorder_traversal :: BST a -> [a]
inorder_traversal Nil = []
inorder_traversal (Node treeLeft root treeRight) = inorder_traversal treeLeft ++ [root] ++ inorder_traversal treeRight


-- Preorder traversal of the tree
preorder_traversal :: BST a -> [a]
preorder_traversal Nil = []
preorder_traversal (Node treeLeft root treeRight) =  [root] ++ preorder_traversal treeLeft ++ preorder_traversal treeRight


-- Postorder traversal of the tree
postorder_traversal :: BST a -> [a]
postorder_traversal Nil = []
postorder_traversal (Node treeLeft root treeRight) =  postorder_traversal treeLeft ++ postorder_traversal treeRight ++ [root]



main :: IO ()
main = do 
       
      -- Prompting the user to enter numbers seperated by commas
      putStrLn ""
      putStrLn "Enter comma separated list of numbers: "
      str <- getLine
      putStrLn ""

      -- Numbers entered by the user, converted into list form are present in my_list
      let integer_list = map (read :: String -> Integer) (splitOn "," str) 
      print integer_list

      let bst = create_tree integer_list

      putStrLn "Inorder Traversal: "
      print (inorder_traversal bst)
      putStrLn ""


      putStrLn "Preorder Traversal: "
      print (preorder_traversal bst)
      putStrLn ""


      putStrLn "postorder Traversal: "
      print (postorder_traversal bst)
      putStrLn ""
