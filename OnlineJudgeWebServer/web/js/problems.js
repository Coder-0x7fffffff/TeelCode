class ProblemsWebModel{
    constructor() {
        //init size
        let width = document.body.clientWidth
        let height = window.screen.availHeight
        let question_container = document.getElementById("question_container")
        let vertical_gap = document.getElementById("vertical_gap")
        let code_container = document.getElementById("code_container")
        question_container.style.width = (width/2-2).toString()+"px"
        code_container.style.width = (width/2-2).toString()+"px"
        monaco.editor.create(document.getElementById("code_container"), {
            value: "function hello() {\n\talert('Hello world!');\n}",
            language: "javascript"
        });
    }
}

new ProblemsWebModel()