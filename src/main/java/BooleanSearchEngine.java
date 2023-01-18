import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String,List<PageEntry>> indexedWord = new HashMap<>();
    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File file : Objects.requireNonNull(pdfsDir.listFiles())) {
            var doc = new PdfDocument(new PdfReader(file));
            for (int i = 1; i <= doc.getNumberOfPages(); i++){
                var page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (String word : freqs.keySet()) {
                    PageEntry pageEntry = new PageEntry(file.getName(), i, freqs.get(word));
                    if (!indexedWord.containsKey(word)) {
                        List<PageEntry> pageEntryList = new ArrayList<>();
                        pageEntryList.add(pageEntry);
                        indexedWord.put(word, pageEntryList);
                    } else {
                        indexedWord.get(word).add(pageEntry);
                    }
                }
            }

        }
    }

    @Override
    public List<PageEntry> search(String word) {

        word = word.toLowerCase();
        List<PageEntry> pageEntryList = indexedWord.getOrDefault(word, Collections.emptyList());
        Collections.sort(pageEntryList);
        return pageEntryList;
    }

}
