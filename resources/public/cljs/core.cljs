(require '[reagent.core :as r]
         '[reagent.dom :as rdom])

;; Router state
(def route-state (r/atom {:current-route :home}))

;; Navigation functions
(defn navigate! [route]
  (swap! route-state assoc :current-route route))

;; Hero section component
(defn hero-section []
  [:div.hero-section
   [:div.container
    [:h2 "Welcome to My Portfolio"]
    [:p "Showcasing my best work and projects."]
    [:a.button {:href "#" :on-click #(navigate! :projects)} "View My Work"]]])

;; Projects section component
(defn projects-section []
  [:div.container
   [:header
    [:h1 "My Projects"]
    [:p "A collection of my recent work."]]
   [:div.projects
    [:div.project
     [:img {:src "project1.jpg" :alt "Project 1"}]
     [:h3 "Project One"]
     [:p "Description of project one."]]
    [:div.project
     [:img {:src "project2.jpg" :alt "Project 2"}]
     [:h3 "Project Two"]
     [:p "Description of project two."]]
    [:div.project
     [:img {:src "project3.jpg" :alt "Project 3"}]
     [:h3 "Project Three"]
     [:p "Description of project three."]]]])

;; About section component
(defn about-section []
  [:div.about-section
   [:div.container
    [:h2 "About Me"]
    [:p "I'm a passionate developer with a focus on creating engaging and user-friendly web experiences. I love to learn new technologies and apply them to solve real-world problems. My experience spans from front-end development using modern JavaScript frameworks to back-end development with various server-side technologies."]]])

;; Contact section component
(defn contact-section []
  [:div.contact-section
   [:div.container
    [:h2 "Connect With Me"]
    [:a {:href "https://github.com/yourusername"} [:img {:src "github.png" :alt "GitHub"}]]
    [:a {:href "https://linkedin.com/in/yourprofile"} [:img {:src "linkedin.png" :alt "LinkedIn"}]]]
   [:footer
    [:div.container
     [:p "2024 My Portfolio"]]]])

;; Navigation component
(def menu-open (r/atom false))

(defn toggle-menu! []
  (swap! menu-open not))

(defn navbar []
  (let [current-route (:current-route @route-state)
        open? @menu-open]
    [:nav.main-nav {:class (when open? "menu-open")}
     [:div.container
      [:div.logo "Your Name"]
      
      ;; Hamburger menu button for mobile
      [:div.menu-toggle {:on-click toggle-menu!}
       [:span]
       [:span]
       [:span]]
      
      [:ul
       [:li [:a {:href "#" 
                 :on-click (fn [e]
                            (.preventDefault e)
                            (navigate! :home)
                            (reset! menu-open false))
                 :class (when (= current-route :home) "active")} "Home"]]
       [:li [:a {:href "#" 
                 :on-click (fn [e]
                            (.preventDefault e)
                            (navigate! :projects)
                            (reset! menu-open false))
                 :class (when (= current-route :projects) "active")} "Projects"]]
       [:li [:a {:href "#" 
                 :on-click (fn [e]
                            (.preventDefault e)
                            (navigate! :about)
                            (reset! menu-open false))
                 :class (when (= current-route :about) "active")} "About"]]
       [:li [:a {:href "#" 
                 :on-click (fn [e]
                            (.preventDefault e)
                            (navigate! :contact)
                            (reset! menu-open false))
                 :class (when (= current-route :contact) "active")} "Contact"]]]]]))


;; Router component
(defn router []
  (let [current-route (:current-route @route-state)]
    (case current-route
      :home [:div [hero-section]]
      :projects [:div [projects-section]]
      :about [:div [about-section]]
      :contact [:div [contact-section]]
      ;; Default route
      [:div [hero-section]])))

;; Main application component
(defn app []
  [:div
   [navbar]
   [router]])

;; Render the component to the DOM
(rdom/render [app] (js/document.getElementById "app"))

;; Initialize hash-based routing (optional)
(defn init-hash-routing! []
  (let [handle-hash-change #(let [hash (.. js/window -location -hash)
                                  route (case hash
                                          "#projects" :projects
                                          "#about" :about
                                          "#contact" :contact
                                          :home)]
                              (navigate! route))]
    (.addEventListener js/window "hashchange" handle-hash-change)
    (handle-hash-change))) ; Handle initial route
