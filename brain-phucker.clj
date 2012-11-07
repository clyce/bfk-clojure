(ns brain-phucker)
(defn brainfk "Process coll as a brainf**k machine, eval exp as a brainf**k expression on it, and then collect the outputs. Returns a hash-map containing the proceeded result (:result) and the collected output(:output), each as a list or a string" 
  [coll exp]
  (let [op-nth #(concat (take %2 %3) [(% (nth %3 %2))] (drop (inc %2) %3))
        bfk-interp 
        (fn [coll exp] 
          (loop [cur-cmd-pos 0 cur-op-pos 0 coll-to-op coll coll-final []]
            (if-not (> (count exp) cur-cmd-pos) {:result coll-to-op :output (apply list coll-final)}
              (case (nth exp cur-cmd-pos)
                \. (recur (inc cur-cmd-pos) cur-op-pos coll-to-op (conj coll-final (nth coll-to-op cur-op-pos)))
                \> (recur (inc cur-cmd-pos) (inc cur-op-pos) (if (= (inc cur-op-pos) (count coll-to-op)) (concat coll-to-op [0]) coll-to-op) coll-final) 
                \< (recur (inc cur-cmd-pos) (if (zero? cur-op-pos) cur-op-pos (dec cur-op-pos)) coll-to-op coll-final)
                \+ (recur (inc cur-cmd-pos) cur-op-pos (op-nth inc cur-op-pos coll-to-op) coll-final)
                \- (recur (inc cur-cmd-pos) cur-op-pos (op-nth dec cur-op-pos coll-to-op) coll-final)
                (or \[ \]) (recur 
                             (loop [jmp-count 0 jmp-searcher cur-cmd-pos]
                               (let [isleft (= (nth exp cur-cmd-pos) \[) judge-zero (if isleft (complement zero?) zero?) fstep (if isleft inc dec) bstep (if isleft dec inc)]
                                 (if (judge-zero (nth coll-to-op cur-op-pos))
                                   (inc cur-cmd-pos)
                                   (case (nth exp (fstep jmp-searcher))
                                     \[ (if (and (not isleft) (= 0 jmp-count)) jmp-searcher (recur (fstep jmp-count) (fstep jmp-searcher)))
                                     \] (if (and isleft (= 0 jmp-count)) (+ 2 jmp-searcher) (recur (bstep jmp-count) (bstep jmp-searcher)))
                                     (recur jmp-count (fstep jmp-searcher)))))) cur-op-pos coll-to-op coll-final)
                (recur (inc cur-cmd-pos) cur-op-pos coll-to-op coll-final)))))]
    (if (string? coll) 
      (let [interp-return (brainfk (map int coll) exp) combine-to-str #(->> % (map char) (apply str))]
        {:result (combine-to-str (:result interp-return)) :output (combine-to-str (:output interp-return))})
      (bfk-interp coll exp))))

(defn bfk-proceed 
  "Process coll as a brainf**k machine, and eval exp as a brainf**k expression on it. Returns the proceeded coll as a list/string" 
  [coll exp] (:result (brainfk coll exp)))

(defn bfk-spawn 
  "Process coll as a brainf**k machine, eval exp as a brainf**k expression on it, and then collect the outputs. Returns the outputs as a list/string" 
  [coll exp] (:output (brainfk coll exp)))
