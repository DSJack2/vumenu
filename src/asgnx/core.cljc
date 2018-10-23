(ns asgnx.core
 (:require [clojure.string :as string]
           [clojure.core.async :as async :refer [go chan <! >!]]
           [asgnx.kvstore :as kvstore
            :refer [put! get! list! remove!]]))


;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
 (if msg
     (string/split msg #" ")
     []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
 (first (words msg)))


;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
 (rest (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
 {:cmd (cmd msg), :args (args msg)})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
 {:to to, :msg msg, :action :send})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
 (map #(action-send-msg % msg) people))

;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; bound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
 {:action :assoc-in, :ks ks, :v v})

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
 (map #(action-insert (conj prefix %) v) ks))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
 {:action :dissoc-in, :ks ks})


;; Asgn 3.
;;
;; @Todo: Create a function called "experts-register"
;; that takes the current application `state`, a `topic`
;; the expert's `id` (e.g., unique name), and information
;; about the expert (`info`) and registers them as an expert on
;; the specified topic. Look at the associated test to see the
;; expected function signature.
;;
;; Your function should NOT directly change the application state
;; to register them but should instead return a list of the
;; appropriate side-effects (above) to make the registration
;; happen.
;;
;; See the integration test in See handle-message-test for the
;; expectations on how your code operates
;;
(defn experts-register [experts topic id info]
 (action-insert [:expert topic id] info))

;; Add a campus dining option to the current state
(defn dining-register [dining id]
 (action-insert [:dining id] {}))

;; Manage the hours of a particular dining option by day of the week
(defn create-hours [dining id day hours]
 (action-insert [:dining id day] {:time hours}))

;; Create/manage menu items for a specific dining option on a specific day of the week
(defn create-item [dining id day item type]
 (action-insert [:dining id :menu day item] {:type type}))

;; Funciton that sends the user the hours for a specific dining option, and sends
;; "Working..." while it is searching for the hours. If there are no hours
;; set for the dining option yet, the user recieves "There are no hours listed for this dining option."

(defn get-hours [dining {:keys [args user-id]}]
 (cond
       (empty? dining) [[] "There are no hours listed for this dining option."]
       :else [[(into {} (action-send-msg user-id (str "The hours on " (last args) " for " (first args) " are " (get-in dining [(last args) :time]))))] "Working..."]))

;; Funciton that sends the user the menu items for a specific dining option on a specific day
;; While searching for the menu options, the user recieves "Looking for menu..."

(defn get-menu [items {:keys [args user-id]}]
 [[into {} (action-send-msg user-id (str (doseq [keyval items] ((key keyval) (val keyval)))))] "Looking for menu..."])

;; Function to add a campus dining option to the current state, and sends the
;; user confirmation when it is finished doing so ("x is now a campus dining option")

(defn add-dining [dining {:keys [args]}]
 [[(dining-register dining (first args))] (str (first args) " is now a campus dining option.")])

;; Function to add/edit hours for a campus dining option for a specific day.
;; User recieves a message saying "Hours for x have been updated" after the action
;; has been executed

(defn edit-hours [dining {:keys [args]}]
 [[(create-hours dining (first args) (first (rest args)) (last args))] (str "Hours for " (first args) " have been updated.")])

;; Function to add/edit menu items for a dining option for a specific day.
;; User recieves a message saying "<item> is now a menu item for <dining option> on <day>"
;; when the action is completed.

(defn add-item [dining {:keys [args]}]
 [[(create-item dining (first args) (first (rest args)) (first (rest (rest args))) (last args))] (str (first (rest (rest args))) " is now a menu item for " (first args) " on " (first (rest args)))])

;; Don't edit!
(defn stateless [f]
 (fn [_ & args]
   [[] (apply f args)]))


(def routes {"add-dining" add-dining
             "edit-hours" edit-hours
             "add-item" add-item
             "hours"    get-hours
             "menu"     get-menu
             "default"  [[] (str "Invalid Command.")]})

;; query to retrieve the hours of the specified dining option
(defn hours-for-dining-query [state-mgr pmsg]
 (let [dining (:args pmsg)]
   (get! state-mgr [:dining (first (:args pmsg))])))

;; query to retrieve menu items for a dining option on a specific day
(defn items-for-dining-query [state-mgr pmsg]
 (let [items (:args pmsg)]
   (get! state-mgr [:dining (first (:args pmsg)) :menu (last args)])))

;; query to add items to the state
(defn add-item-query [state-mgr pmsg]
 (let [items (:args pmsg)]
   (get! state-mgr [:dining (first (:args pmsg))])))

;; Don't edit!
(def queries
 {
  "edit-hours" hours-for-dining-query
  "hours"  hours-for-dining-query})


;; Don't edit!
(defn read-state [state-mgr pmsg]
 (go
   (if-let [qfn (get queries (:cmd pmsg))]
     (<! (qfn state-mgr pmsg))
     {})))


;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;

(defn create-router [routes]
 (fn [pmsg]
  (if (contains? routes (:cmd pmsg))
    (get routes (get pmsg :cmd) (get pmsg :args))
    (get routes "default"))))

;; Don't edit!
(defn output [o]
 (second o))


;; Don't edit!
(defn actions [o]
 (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
 (go
   (println "    Invoke:" e)
   (if-let [action (get effect-handlers (:action e))]
     (do
       (println "    Invoking:" action "with" e)
       (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
 (go
   (println "  Processing actions:" actions)
   (let [results (atom [])]
     (doseq [action actions]
       (let [result (<! (invoke system action))]
         (swap! results conj result)))
     @results)))


;; Don't edit!
(defn handle-message
 "
   This function orchestrates the processing of incoming messages
   and glues all of the pieces of the processing pipeline together.

   The basic flow to handle a message is as follows:

   1. Create the router that will be used later to find the
      function to handle the message
   2. Parse the message
   3. Load any saved state that is going to be needed to process
      the message (e.g., querying the list of experts, etc.)
   4. Find the function that can handle the message
   5. Call the handler function with the state from #3 and
      the message
   6. Run the different actions that the handler returned...these actions
      will be bound to different implementations depending on the environemnt
      (e.g., in test, the actions aren't going to send real text messages)
   7. Return the string response to the message

 "
 [{:keys [state-mgr] :as system} src msg]
 (go
   (println "=========================================")
   (println "  Processing:\"" msg "\" from" src)
   (let [rtr    (create-router routes)
         _      (println "  Router:" rtr)
         pmsg   (assoc (parsed-msg msg) :user-id src)
         _      (println "  Parsed msg:" pmsg)
         state  (<! (read-state state-mgr pmsg))
         _      (println "  Read state:" state)
         hdlr   (rtr pmsg)
         _      (println "  Hdlr:" hdlr)
         [as o] (hdlr state pmsg)
         _      (println "  Hdlr result:" [as o])
         arslt  (<! (process-actions system as))
         _      (println "  Action results:" arslt)]
     (println "=========================================")
     o)))
