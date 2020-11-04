package com.example.POCBatchZip.BatchConfig;



import com.example.POCBatchZip.UpDownUtil.UpdownUtil;
import com.example.POCBatchZip.ZipUtil.ZipUtil;
import net.lingala.zip4j.ZipFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ZipUtil zip;

    @Autowired
    private UpdownUtil updownUtil;

    List<List<ZipFile>> zipFiles = new ArrayList<>();



    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(splitFlow())
                .build()        //Build dell'istanza FlowBuilder
                .build();       //Build dell'istanza Job
    }


    /// Impostiamo il Tasklet multithread
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(4);
        return simpleAsyncTaskExecutor;
    }


    /// Impostiamo lo spitflow e il flow

    @Bean
    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flow1())
                .build();
    }



    @Bean
    public Flow flow1() {
        return new FlowBuilder<SimpleFlow>("flow")
                .start(step1())
                .next(step2())
                .build();
    }


    /// Scorriamo le cartelle e creiamo lo ZIP
    Step step1(){
        return stepBuilderFactory
                .get("step1")
                .tasklet((StepContribution contribution, ChunkContext chunk ) -> {
                            // Scorrere la cartella
                            Path dir = Paths.get("C:\\Users\\sdicostanzo\\Desktop\\DownFTP");

                            Stream<Path> walk = Files.walk(dir,1) ;

                            List <Path> listDir = walk.filter(p -> Files.isDirectory(p) && ! p.equals(dir))
                                       .collect(Collectors.toList());

                            for(Path Dir : listDir ){
                                try (Stream<Path> walk1 = Files.walk(Dir,1)) {
                                    // Contiene tutte le schede all'interno del Path sotto \DownFTP\ClasseDocumentale -> \DownFTP\Fatture
                                    // Contenuto di listSubDir -> ..\DownFTP\FattureElettroniche, ..\DownFTP\FattureCartacee, etc..
                                    List <String> listSubDir = walk1.filter(p -> Files.isDirectory(p) && ! p.equals(dir) && ! listDir.contains(p))
                                            .map(x -> x.toString()).collect(Collectors.toList());

                                    /// createZip(ClasseDocumentale,Lista schede,Dir principale (ES: \DownFTP), limite schede)
                                    /// Una volta terminata la  creazione il file viene Uplodato sul server FTP

                                    zipFiles.add(zip.createZips(Dir,listSubDir,dir,2));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            return RepeatStatus.FINISHED;
                        }
                )
                .build();
    }


    /// Carichiamo lo ZIP Creato su FTP
    Step step2(){
        return stepBuilderFactory
                .get("step2")
                .tasklet((StepContribution contribution, ChunkContext chunk ) -> {
                            /// Prendi ZIP e carica su FTP
                            File[] files = new File("C:\\Users\\sdicostanzo\\Desktop\\DownFTP").listFiles();
                            for (File file : files) {
                                if (file.isFile())
                                    updownUtil.upload(file);
                            }
                            return RepeatStatus.FINISHED;
                        }
                )
                .build();

    }
}
