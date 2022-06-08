module GenLexicon
  class Generator < Jekyll::Generator
    def generate(site)
      lex_config = site.config["lexicon"]
      script = lex_config["script"]
      base_path = lex_config["filepath"]
      mod_name = lex_config["mod_name"]
      book_name = lex_config["book_name"]

      script_run = `#{script} #{base_path} #{mod_name} #{book_name}`

      lexicon = site.pages.find { |page| page.name == 'lexicon.html'}

      lexicon.data['body'] = script_run
    end
  end
end
